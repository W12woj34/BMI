package com.example.bmi

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_history.*


class HistoryActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val prefs = SharedPreference(this)
        val records = prefs.getRecordList(MainActivity.KEY_BMI_RESULTS)
        records.reverse()
        historyView.layoutManager = LinearLayoutManager(this)
        historyView.adapter = ResultAdapter(records)




    }
}