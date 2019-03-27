package com.example.bmi

import android.content.Intent
import android.graphics.ColorFilter
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
        outState?.putString(getString(R.string.BMI_NUMBER), bmiNumberView.text.toString())
        outState?.putString(getString(R.string.BMI_DESCRIPTION), bmiDescriptionView.text.toString())
        outState?.putInt(getString(R.string.BMI_COLOR), bmiNumberView.currentTextColor)
        outState?.putString(getString(R.string.BMI_MASS), massText.text.toString())
        outState?.putString(getString(R.string.BMI_HEIGHT), heightText.text.toString())
        outState?.putBoolean(getString(R.string.BMI_UNITS), units)
        outState?.putInt(getString(R.string.BMI_VISIBILITY), showInfo.visibility)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState != null){
            bmiNumberView.text = savedInstanceState.getString(getString(R.string.BMI_NUMBER))
            bmiNumberView.setTextColor(savedInstanceState.getInt(getString(R.string.BMI_COLOR)))
            bmiDescriptionView.text = savedInstanceState.getString(getString(R.string.BMI_DESCRIPTION))
            massText.text = savedInstanceState.getString(getString(R.string.BMI_MASS))
            heightText.text = savedInstanceState.getString(getString(R.string.BMI_HEIGHT))
            units = savedInstanceState.getBoolean(getString(R.string.BMI_UNITS))
            showInfo.visibility = savedInstanceState.getInt(getString(R.string.BMI_VISIBILITY))
            showInfo.setBackgroundColor(savedInstanceState.getInt(getString(R.string.BMI_COLOR)))
        }

    }

    private fun countBmiButton(){
        var error = false
        if(massEdit.text.isEmpty() || massEdit.text.length > 3 || massEdit.text.toString().toInt() == 0){
            massEdit.error = getString(R.string.BMI_MASS_ERROR)
            error = true
        }

        if(heightEdit.text.isEmpty() || heightEdit.text.length > 3 || heightEdit.text.toString().toInt() == 0){
            heightEdit.error = getString(R.string.BMI_HEIGHT_ERROR)
            error = true
        }

        if(!error){
            val bmi : Bmi
            val mass = massEdit.text.toString().toInt()
            val height = heightEdit.text.toString().toInt()
            if(!units){
                bmi = BmiForKgCm(mass, height)
            }else{
                bmi = BmiForLbIn(mass, height)
            }
            this.bmiNumberView.text = (((bmi.countBmi()*100).toInt())/100.0).toString()
            this.bmiNumberView.setTextColor(ContextCompat.getColor(this, bmiClassification(bmi.countBmi()).second))
            this.bmiDescriptionView.text = bmiClassification(bmi.countBmi()).first
            this.showInfo.visibility = View.VISIBLE
            this.showInfo.setBackgroundColor(bmiNumberView.currentTextColor)
        }

    }

    private fun showInfoButton(){
        if(bmiNumberView.text != ""){
            val dataIntent = Intent(this, InfoBmiActivity::class.java)
            dataIntent.putExtra(getString(R.string.BMI_VALUE), bmiNumberView.text.toString())
            dataIntent.putExtra(getString(R.string.BMI_NAME), bmiDescriptionView.text.toString())
            dataIntent.putExtra(getString(R.string.BMI_COLOR), bmiNumberView.currentTextColor)
            dataIntent.putExtra(getString(R.string.BMI_DESCRIPTION), chooseDescription(bmiDescriptionView.text.toString()))
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
        showInfo.visibility = View.GONE
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

}
