package com.sn.mapd721_w2023_p1_group_10

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class forecastModelAdapter (var mCtx: Context, var resources:Int, var items:List<forecastModel>):
    ArrayAdapter<forecastModel>(mCtx, resources, items) {
    //These are our parameters

    //Then Creating a function
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(resources, null)

        val imageView: ImageView = view.findViewById(R.id.rowForecastImageVw)
        val tempTextView: TextView = view.findViewById(R.id.rowTempTxt)
        val descriptionTextView: TextView = view.findViewById(R.id.rowDescriptionTxt)
        val dateTextView: TextView = view.findViewById(R.id.rowDateTxt)


        val mItem:forecastModel = items[position]
        imageView.setImageDrawable(mCtx.resources.getDrawable(mItem.forecastIcon))
        tempTextView.text = mItem.temp
        descriptionTextView.text = mItem.description
        dateTextView.text = mItem.date

        return view
    }
}