package com.farifam.kotlinfirestore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView

import kotlinx.android.synthetic.main.row_data.*
import java.util.*

/**
 * Created by sabithuraira on 10/21/17.
 */


class DataAdapter(private val dataSet: ArrayList<Member>, mContext: Context) : BaseAdapter() {

    private val inflater = mContext.layoutInflater
    lateinit var view :View

    override fun getItem(position: Int): Any = mDays[position]
    override fun getCount(): Int = mDays.size
    override fun getItemId(position: Int): Long = 0//not used


    private class ViewHolder {
        internal var report_category: TextView? = null
        internal var report_sub: TextView? = null
        internal var report_detail: TextView? = null
        internal var report_description: TextView? = null
        internal var report_date: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val dataModel = getItem(position)
        val viewHolder: DataAdapter.ViewHolder

        val result: View

        if (convertView == null) {
            viewHolder = DataAdapter.ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.row_data, parent, false)
//            viewHolder.report_category = convertView!!.findViewById(R.id.report_category) as TextView
//            viewHolder.report_sub = convertView.findViewById(R.id.report_sub) as TextView
//            viewHolder.report_detail = convertView.findViewById(R.id.report_detail) as TextView
//            viewHolder.report_description = convertView.findViewById(R.id.report_description) as TextView
//            viewHolder.report_date = convertView.findViewById(R.id.report_date) as TextView

            result = convertView

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ReportAdapter.ViewHolder
            result = convertView
        }

        lastPosition = position

        viewHolder.report_category!!.setText(dataModel!!.getMSREPORTCATEGORYNAME())
        viewHolder.report_sub!!.setText(dataModel!!.getMSSUBREPORTCATEGORYNAME())
        viewHolder.report_description!!.setText(dataModel!!.getDESCRIPTION())
        viewHolder.report_detail!!.setText(dataModel!!.getMSDETAILREPORTCATEGORYNAME())
        viewHolder.report_date!!.setText(dataModel!!.getREPORTDATE())


        return convertView
    }
}