package com.example.bmi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.bmi.logic.Bmi
import com.example.bmi.logic.BmiForKgCm
import com.example.bmi.logic.BmiForLbIn
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var units = false
    private lateinit var prefs: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = SharedPreference(this)

        countBmi.setOnClickListener {
            countBmiButton()
        }

        showInfo.setOnClickListener {
            showInfoButton()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val records = prefs.getRecordList(KEY_BMI_RESULTS)
        if (records.isEmpty()) {
            menu!!.getItem(2).isEnabled = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if (id == R.id.switchUnits) {
            switchUnits()
            return true
        }

        if (id == R.id.aboutMe) {
            startActivity(Intent(this, AboutMeActivity::class.java))
            return true
        }

        if (id == R.id.history) {
                startActivity(Intent(this, HistoryActivity::class.java))
            return true
        }



        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(KEY_BMI_VALUE, bmiNumberView.text.toString())
        outState?.putString(KEY_BMI_DESCRIPTION, bmiDescriptionView.text.toString())
        outState?.putInt(KEY_BMI_COLOR, bmiNumberView.currentTextColor)
        outState?.putString(KEY_BMI_MASS, massText.text.toString())
        outState?.putString(KEY_BMI_HEIGHT, heightText.text.toString())
        outState?.putBoolean(KEY_BMI_UNITS, units)
        outState?.putInt(KEY_BMI_VISIBILITY, showInfo.visibility)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            bmiNumberView.text = savedInstanceState.getString(KEY_BMI_VALUE)
            bmiNumberView.setTextColor(savedInstanceState.getInt(KEY_BMI_COLOR))
            bmiDescriptionView.text = savedInstanceState.getString(KEY_BMI_DESCRIPTION)
            massText.text = savedInstanceState.getString(KEY_BMI_MASS)
            heightText.text = savedInstanceState.getString(KEY_BMI_HEIGHT)
            units = savedInstanceState.getBoolean(KEY_BMI_UNITS)
            showInfo.visibility = savedInstanceState.getInt(KEY_BMI_VISIBILITY)
        }

    }

    private fun countBmiButton() {

        if (!checkIfEmpty()) {
            val bmi: Bmi
            val mass = massEdit.text.toString().toInt()
            val height = heightEdit.text.toString().toInt()
            if (!units) {
                bmi = BmiForKgCm(mass, height)
            } else {
                bmi = BmiForLbIn(mass, height)
            }

            when {
                (bmi.getMass() !in bmi.getMassRange()) && (bmi.getHeight() !in bmi.getHeightRange()) -> {
                    showMassError()
                    showHeightError()
                }
                bmi.getMass() !in bmi.getMassRange() -> showMassError()
                bmi.getHeight() !in bmi.getHeightRange() -> showHeightError()
                else -> {
                    val bmiValue = ((bmi.countBmi() * 100).toInt() / 100.0).toString()
                    val category = bmiClassification(bmi.countBmi())
                    this.bmiNumberView.text = bmiValue
                    this.bmiNumberView.setTextColor(category.getColor(this.resources))
                    this.bmiDescriptionView.text = category.getName(this.resources)
                    this.showInfo.visibility = View.VISIBLE
                    updateHistory(bmiValue, category.getName(this.resources), category.getColor(this.resources))
                    invalidateOptionsMenu()
                }
            }


        }
    }

    private fun updateHistory(bmiValue: String, category: String, color: Int) {
        val date = Calendar.getInstance().time
        val dateText = SimpleDateFormat(getString(R.string.BMI_DATE_FORMAT), Locale.getDefault()).format(date)
        val massString = getString(R.string.BMI_MASS_STRING_START) + massEdit.text + if (!units) {
            getString(R.string.BMI_MASS_STRING_END_KG)
        } else {
            getString(R.string.BMI_MASS_STRING_END_LB)
        }
        val heightString = getString(R.string.BMI_HEIGHT_STRING_START) + heightEdit.text + if (!units) {
            getString(R.string.BMI_HEIGHT_STRING_END_CM)
        } else {
            getString(R.string.BMI_HEIGHT_STRING_END_IN)
        }
        val bmiRecord = BmiRecord(massString, heightString, bmiValue, category, color, dateText)

        val records = prefs.getRecordList(KEY_BMI_RESULTS)
        if (records.size < 10) {
            records.add(bmiRecord)
            prefs.save(KEY_BMI_RESULTS, records)
        } else {
            records.removeAt(0)
            records.add(bmiRecord)
            prefs.save(KEY_BMI_RESULTS, records)
        }
    }

    private fun showMassError() {
        massEdit.error = getString(R.string.BMI_MASS_ERROR)
    }

    private fun showHeightError() {
        heightEdit.error = getString(R.string.BMI_HEIGHT_ERROR)
    }

    private fun checkIfEmpty(): Boolean {
        var error = false
        if (massEdit.text.isEmpty()) {
            showMassError()
            error = true
        }

        if (heightEdit.text.isEmpty()) {
            showHeightError()
            error = true
        }

        return error
    }

    private fun showInfoButton() {
        if (bmiNumberView.text != getString(R.string.BMI_BLANK)) {
            val dataIntent = Intent(this, InfoBmiActivity::class.java)
            dataIntent.putExtra(KEY_BMI_VALUE, bmiNumberView.text.toString())
            dataIntent.putExtra(KEY_BMI_NAME, bmiDescriptionView.text.toString())
            dataIntent.putExtra(KEY_BMI_COLOR, bmiNumberView.currentTextColor)
            dataIntent.putExtra(
                KEY_BMI_DESCRIPTION,
                bmiClassification(bmiNumberView.text.toString().toDouble()).getDescription(this.resources)
            )
            startActivity(dataIntent)
        }
    }

    private fun switchUnits() {
        units = !units

        when {
            !units -> {
                massText.text = getString(R.string.BMI_MASS_KG)
                heightText.text = getString(R.string.BMI_HEIGHT_CM)
            }
            else -> {
                massText.text = getString(R.string.BMI_MASS_LB)
                heightText.text = getString(R.string.BMI_HEIGHT_IN)
            }
        }
        bmiNumberView.text = getString(R.string.BMI_BLANK)
        bmiDescriptionView.text = getString(R.string.BMI_BLANK)
        massEdit.text = null
        heightEdit.text = null
        showInfo.visibility = View.INVISIBLE
        Toast.makeText(this, getString(R.string.BMI_SWITCH_COM), Toast.LENGTH_LONG).show()
    }

    private fun bmiClassification(bmi: Double): BmiCategories {

        return when {
            bmi < 18.5 -> BmiCategories.UNDERWEIGHT
            bmi in 18.5..25.0 -> BmiCategories.NORMAL
            bmi in 25.0..30.0 -> BmiCategories.OVERWEIGHT
            bmi in 30.0..35.0 -> BmiCategories.OBESE1
            bmi in 35.0..40.0 -> BmiCategories.OBESE2
            else -> BmiCategories.OBESE3
        }
    }


    companion object {
        const val KEY_BMI_COLOR = "BMI_COLOR"
        const val KEY_BMI_VALUE = "BMI_VALUE"
        const val KEY_BMI_NAME = "BMI_NAME"
        const val KEY_BMI_DESCRIPTION = "BMI_DESCRIPTION"
        const val KEY_BMI_VISIBILITY = "BMI_VISIBILITY"
        const val KEY_BMI_UNITS = "BMI_UNITS"
        const val KEY_BMI_MASS = "BMI_MASS"
        const val KEY_BMI_HEIGHT = "BMI_HEIGHT"
        const val KEY_BMI_RESULTS = "BMI_RESULTS"
    }
}
