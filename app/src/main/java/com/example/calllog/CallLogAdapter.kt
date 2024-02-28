package com.example.calllog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlin.time.Duration

class CallLogAdapter(context: Context) : ArrayAdapter<String>(context, R.layout.call_log_item) {

    private val callLogs: MutableList<String> = mutableListOf()
    private val groupedCallLogs: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun addCallLog(number: String, duration: String){

        if (groupedCallLogs.containsKey(number)){
            groupedCallLogs[number]?.add(duration)
        } else{
            val durations = mutableListOf(duration)
            groupedCallLogs[number] = durations
            callLogs.add(number)
        }

        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return callLogs.size
    }

    override fun getItem(position: Int): String {
        return callLogs[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView
        if (rowView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.call_log_item, parent, false)
        }

        val number = getItem(position)
        val durations = groupedCallLogs[number]

        val numberTextView: TextView = rowView!!.findViewById(R.id.numberTextView)
        val durationTextView: TextView = rowView.findViewById(R.id.durationTextView)

        numberTextView.text = number
        durationTextView.text = formatDurations(durations)

        return rowView
    }

    private fun formatDurations(durations: List<String>?): String {

        var totalDuration = 0
        durations?.forEach { duration ->
            totalDuration += duration.toInt()
        }

        return totalDuration.toString()
    }
}