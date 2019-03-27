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
            bmiValue.text = bundle.getString(getString(R.string.BMI_VALUE))
            bmiValue.setTextColor(bundle.getInt(getString(R.string.BMI_COLOR)))
            bmiClass.text = bundle.getString(getString(R.string.BMI_NAME))
            bmiClass.setTextColor(bundle.getInt(getString(R.string.BMI_COLOR)))
            bmiDescriptionLong.text = bundle.getString(getString(R.string.BMI_DESCRIPTION))
        }

    }
}
