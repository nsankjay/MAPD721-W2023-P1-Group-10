package com.sn.mapd721_w2023_p1_group_10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class WeatherReminderListViewActivity2 : AppCompatActivity(), UpdateAndDelete {

    lateinit var database: DatabaseReference

    var reminderLIst: MutableList<ReminderModel>? = null
    lateinit var adapter: ReminderAdapter
    private var listViewItem : ListView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_reminder_list_view2)

        val addReminderFabBtn = findViewById<View>(R.id.reminderAddFabBtn) as FloatingActionButton

        listViewItem = findViewById<ListView>(R.id.reminderListView2)

        database = FirebaseDatabase.getInstance().reference

        addReminderFabBtn.setOnClickListener{
            val alertDialog = AlertDialog.Builder(this)
            val textEditText = EditText(this)
            val dateEditText = EditText(this)
            alertDialog.setMessage("Add Weather Reminder")
            alertDialog.setTitle("Enter Weather Reminder")
            alertDialog.setView(textEditText)
            //alertDialog.setView(dateEditText)
            alertDialog.setPositiveButton("Add") { dialog, i ->
                val reminderItemData = ReminderModel.createList()
                reminderItemData.reminderDataText = textEditText.text.toString()
                reminderItemData.reminderDateText = dateEditText.text.toString()
                reminderItemData.snooze = false

                val newReminderItemData = database.child("reminder").push()
                reminderItemData.UID = newReminderItemData.key

                newReminderItemData.setValue(reminderItemData)

                dialog.dismiss()
                Toast.makeText(this, "Reminder Saved", Toast.LENGTH_LONG).show()

            }
            alertDialog.show()

        }

        reminderLIst = mutableListOf<ReminderModel>()
        adapter = ReminderAdapter(this, reminderLIst!!)
        listViewItem!!.adapter = adapter
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                reminderLIst!!.clear()
                addItemToList(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Reminder Not Saved", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun addItemToList(snapshot: DataSnapshot) {

        val items = snapshot.children.iterator()

        if (items.hasNext()) {
            val reminderIndexedValue = items.next()
            val itemsIterator = reminderIndexedValue.children.iterator()

            while (itemsIterator.hasNext()) {
                val currentItem = itemsIterator.next()
                val reminderItemData =ReminderModel.createList()
                val map = currentItem.getValue() as HashMap<String, Any>

                reminderItemData.UID = currentItem.key
                reminderItemData.snooze = map.get("snooze") as Boolean?
                reminderItemData.reminderDataText = map.get("reminderDataText") as String?
                reminderItemData.reminderDateText = map.get("reminderDateText") as String?
                reminderLIst!!.add(reminderItemData)
            }
        }

        adapter.notifyDataSetChanged()

    }

    override fun modifyItem(itemUID: String, isSnooz: Boolean) {
        val itemReference = database.child("reminder").child(itemUID)
        itemReference.child("snooze").setValue(isSnooz)
    }

    override fun onItemDelete(itemUID: String) {
        val itemReference = database.child("reminder").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }
}