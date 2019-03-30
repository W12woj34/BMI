package com.example.bmi

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class ResultAdapter(private val items: ArrayList<BmiRecord>) : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.history_record, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = items[position]

        holder.mass.text = record.massString
        holder.height.text = record.heightString
        holder.value.text = record.bmiValue
        holder.value.setTextColor(record.color)
        holder.image.setBackgroundColor(record.color)
        holder.category.text = record.category
        holder.date.text = record.date
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val mass: TextView = itemView.findViewById(R.id.historyMass)
        val height: TextView = itemView.findViewById(R.id.historyHeight)
        val value: TextView = itemView.findViewById(R.id.historyBmi)
        val category: TextView = itemView.findViewById(R.id.historyBmiCategory)
        val image: ImageView = itemView.findViewById(R.id.historyImage)
        val date: TextView = itemView.findViewById(R.id.historyDate)

    }
}
