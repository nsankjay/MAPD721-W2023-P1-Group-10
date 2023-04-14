package com.sn.mapd721_w2023_p1_group_10

class ReminderModel {

    companion object Factory {
        fun createList(): ReminderModel = ReminderModel()
    }

    var UID: String? = null
    var reminderDataText: String? = null
    var reminderDateText: String? = null
    var snooze: Boolean? = false

}