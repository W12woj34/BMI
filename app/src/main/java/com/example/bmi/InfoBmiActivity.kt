package com.example.bmi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_info_bmi.*

class InfoBmiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_bmi)

        val bundle = this.intent.extras
        if(bundle != null){
            bmiValue.text = bundle.getString(KEY_BMI_VALUE)
            bmiValue.setTextColor(bundle.getInt(KEY_BMI_COLOR))
            bmiClass.text = bundle.getString(KEY_BMI_NAME)
            bmiClass.setTextColor(bundle.getInt(KEY_BMI_COLOR))
            bmiDescriptionLong.text = bundle.getString(KEY_BMI_DESCRIPTION)
        }

    }

    companion object {
        const val KEY_BMI_COLOR = "BMI_COLOR"
        const val KEY_BMI_VALUE = "BMI_VALUE"
        const val KEY_BMI_NAME = "BMI_NAME"
        const val KEY_BMI_DESCRIPTION = "BMI_DESCRIPTION"
    }
}
