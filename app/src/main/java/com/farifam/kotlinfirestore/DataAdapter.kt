package com.farifam.kotlinfirestore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

//import kotlinx.android.synthetic.main.row_data.*
//import org.w3c.dom.Text
import java.util.*

class DataAdapter(private val dataSet: ArrayList<Member>, internal var mContext: Context) : ArrayAdapter<Member>(mContext, R.layout.row_data, dataSet) {
    private class ViewHolder {
        internal var txt_name: TextView? = null
        internal var txt_born: TextView? = null
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
            viewHolder.txt_name = convertView!!.findViewById<TextView>(R.id.txt_name) as TextView
            viewHolder.txt_born = convertView.findViewById<TextView>(R.id.txt_born) as TextView

            result = convertView

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as DataAdapter.ViewHolder
            result = convertView
        }


        viewHolder.txt_name!!.setText(dataModel!!.first_name + " " + dataModel!!.last_name)
        viewHolder.txt_born!!.setText(dataModel!!.born)


        return convertView
    }
}