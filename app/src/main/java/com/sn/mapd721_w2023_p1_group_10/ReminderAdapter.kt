package com.sn.mapd721_w2023_p1_group_10

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView

class ReminderAdapter (context: Context, reminderList: MutableList<ReminderModel>) : BaseAdapter() {

    private val inflater:LayoutInflater = LayoutInflater.from(context)
    private var reminderList = reminderList
    private var updateAndDelete:UpdateAndDelete = context as UpdateAndDelete


    override fun getCount(): Int {
        return reminderList.size
    }

    override fun getItem(position: Int): Any {
        return reminderList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val UID: String = reminderList.get(position).UID as String
        val reminderDataText = reminderList.get(position).reminderDataText as String
        val reminderDateText = reminderList.get(position).reminderDateText as String
        val snooze = reminderList.get(position).snooze as Boolean

        val view: View
        val viewHolder : ListViewHolder

        if(convertView == null){
            view = inflater.inflate(R.layout.each_reminder, parent,false)
            viewHolder = ListViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ListViewHolder
        }

        viewHolder.textLabel.text = reminderDataText
        viewHolder.dateLabel.text = reminderDateText
        viewHolder.isSnoozed.isChecked = snooze

        viewHolder.isSnoozed.setOnClickListener {
            updateAndDelete.modifyItem(UID, !snooze)
        }

        viewHolder.isDeleted.setOnClickListener {
            updateAndDelete.onItemDelete(UID)
        }

        return view
    }

    private class ListViewHolder (row: View?) {

        val textLabel: TextView = row!!.findViewById(R.id.reminderNameTextTv) as TextView
        val dateLabel: TextView = row!!.findViewById(R.id.reminderDateTextTv) as TextView
        val isSnoozed: CheckBox = row!!.findViewById(R.id.reminderCompleteCheckBox) as CheckBox
        val isDeleted: ImageButton = row!!.findViewById(R.id.reminderCloseDeleteBtn) as ImageButton


    }

}