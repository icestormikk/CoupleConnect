package com.icestormikk.coupleconnect.utilities.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icestormikk.coupleconnect.database.entities.Statistics
import com.icestormikk.coupleconnect.databinding.LayoutStatisticsCardBinding
import kotlin.math.abs

class StatisticsAdapter(
    private val statistics: MutableList<Statistics> = mutableListOf()
): RecyclerView.Adapter<StatisticsAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val statisticsCardBinding: LayoutStatisticsCardBinding
    ) : RecyclerView.ViewHolder(statisticsCardBinding.root) {
        fun bind(statistics: Statistics) {
            fun getColorByCompare(first: Long, second: Long): Int =
                if (first < second) Color.parseColor("#FF4CAF50") else Color.BLACK

            val (propertyName, oldValue, newValue) = statistics

            with (statisticsCardBinding) {
                propertyNameText.text = propertyName
                differenceText.text = String.format("%d -> %d", oldValue, newValue)
                totalCount.text = abs(newValue - oldValue).toString()

                arrayOf(propertyNameText, differenceText, totalCount).forEach {
                    it.setTextColor(getColorByCompare(oldValue, newValue))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutStatisticsCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = statistics.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stat = statistics[position]
        holder.bind(stat)
    }
}
