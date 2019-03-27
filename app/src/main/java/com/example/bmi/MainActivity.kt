package com.example.bmi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.bmi.logic.Bmi
import com.example.bmi.logic.BmiForKgCm
import com.example.bmi.logic.BmiForLbIn
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var units = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countBmi.setOnClickListener {
            countBmiButton()
        }

        showInfo.setOnClickListener {
            showInfoButton()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if(id == R.id.aboutMe){
            startActivity(Intent(this, AboutMeActivity::class.java))
            return true
        }

        if(id == R.id.switchUnits){
            switchUnits()
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
        if(savedInstanceState != null){
            bmiNumberView.text = savedInstanceState.getString(KEY_BMI_VALUE)
            bmiNumberView.setTextColor(savedInstanceState.getInt(KEY_BMI_COLOR))
            bmiDescriptionView.text = savedInstanceState.getString(KEY_BMI_DESCRIPTION)
            massText.text = savedInstanceState.getString(KEY_BMI_MASS)
            heightText.text = savedInstanceState.getString(KEY_BMI_HEIGHT)
            units = savedInstanceState.getBoolean(KEY_BMI_UNITS)
            showInfo.visibility = savedInstanceState.getInt(KEY_BMI_VISIBILITY)
            showInfo.setBackgroundColor(savedInstanceState.getInt(KEY_BMI_COLOR))
        }

    }

    private fun countBmiButton(){

        if(!checkIfEmpty()){
            val bmi : Bmi
            val mass = massEdit.text.toString().toInt()
            val height = heightEdit.text.toString().toInt()
            if(!units){
                bmi = BmiForKgCm(mass, height)
            }else{
                bmi = BmiForLbIn(mass, height)
            }
            when {
                bmi.countBmi() == -1.0 -> {
                    massEdit.error = getString(R.string.BMI_MASS_ERROR)
                    return
                }
                bmi.countBmi() == -2.0 -> {
                    heightEdit.error = getString(R.string.BMI_HEIGHT_ERROR)
                    return
                }
                bmi.countBmi() == -3.0 -> {
                    massEdit.error = getString(R.string.BMI_MASS_ERROR)
                    heightEdit.error = getString(R.string.BMI_HEIGHT_ERROR)
                    return
                }
                else -> {
                    this.bmiNumberView.text = (((bmi.countBmi()*100).toInt())/100.0).toString()
                    this.bmiNumberView.setTextColor(ContextCompat.getColor(this, bmiClassification(bmi.countBmi()).second))
                    this.bmiDescriptionView.text = bmiClassification(bmi.countBmi()).first
                    this.showInfo.visibility = View.VISIBLE
                    this.showInfo.setBackgroundColor(bmiNumberView.currentTextColor)
                }
            }
        }
    }

    private fun checkIfEmpty(): Boolean{
        var error = false
        if(massEdit.text.isEmpty()){
            massEdit.error = getString(R.string.BMI_MASS_ERROR)
            error = true
        }

        if(heightEdit.text.isEmpty()){
            heightEdit.error = getString(R.string.BMI_HEIGHT_ERROR)
            error = true
        }

        return error
    }

    private fun showInfoButton(){
        if(bmiNumberView.text != ""){
            val dataIntent = Intent(this, InfoBmiActivity::class.java)
            dataIntent.putExtra(KEY_BMI_VALUE, bmiNumberView.text.toString())
            dataIntent.putExtra(KEY_BMI_NAME, bmiDescriptionView.text.toString())
            dataIntent.putExtra(KEY_BMI_COLOR, bmiNumberView.currentTextColor)
            dataIntent.putExtra(KEY_BMI_DESCRIPTION, chooseDescription(bmiDescriptionView.text.toString()))
            startActivity(dataIntent)
        }
    }

    private fun chooseDescription(bmiDesc:String) : String{
        if(bmiDesc == getString(R.string.BMI_UNDERWEIGHT)){
            return getString(R.string.BMI_UNDERWEIGHT_INFO)
        }
        if(bmiDesc == getString(R.string.BMI_NORMAL)){
            return getString(R.string.BMI_NORMAL_INFO)
        }
        if(bmiDesc == getString(R.string.BMI_OVERWEIGHT)){
            return getString(R.string.BMI_OVERWEIGHT_INFO)
        }
        if(bmiDesc == getString(R.string.BMI_OBESE1)){
            return getString(R.string.BMI_OBESE1_INFO)
        }
        if(bmiDesc == getString(R.string.BMI_OBESE2)){
            return getString(R.string.BMI_OBESE2_INFO)
        }
        if(bmiDesc == getString(R.string.BMI_OBESE3)){
            return getString(R.string.BMI_OBESE3_INFO)
        }
        return getString(R.string.BMI_ERROR)
    }

    private fun switchUnits(){
        units = !units

        if(!units){
            massText.text = getString(R.string.BMI_MASS_KG)
            heightText.text = getString(R.string.BMI_HEIGHT_CM)
        }
        else{
            massText.text = getString(R.string.BMI_MASS_LB)
            heightText.text = getString(R.string.BMI_HEIGHT_IN)
        }
        bmiNumberView.text = getString(R.string.BMI_BLANK)
        bmiDescriptionView.text = getString(R.string.BMI_BLANK)
        massEdit.text = null
        heightEdit.text = null
        showInfo.visibility = View.INVISIBLE
        Toast.makeText(this,getString(R.string.BMI_SWITCH_COM), Toast.LENGTH_LONG).show()
    }

    private fun bmiClassification(bmi: Double): Pair<String, Int> {

        if(bmi < 18.5 ) return Pair(getString(R.string.BMI_UNDERWEIGHT),R.color.colorLapisLazuli)
        if (bmi >= 18.5 && bmi < 25) return Pair(getString(R.string.BMI_NORMAL), R.color.colorVerdigris)
        if (bmi >= 25 && bmi < 30) return Pair(getString(R.string.BMI_OVERWEIGHT), R.color.colorOrange)
        if (bmi >= 30 && bmi < 35) return Pair(getString(R.string.BMI_OBESE1), R.color.colorPink)
        if (bmi >= 35 && bmi < 40) return Pair(getString(R.string.BMI_OBESE2),R.color.colorPompeianPink)
        if (bmi >= 40) return Pair(getString(R.string.BMI_OBESE3), R.color.colorClaret)
        return Pair(getString(R.string.BMI_ERROR), R.color.colorBlack)
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
    }
}
