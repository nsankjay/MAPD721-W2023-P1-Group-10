package com.sn.mapd721_w2023_p1_group_10

interface UpdateAndDelete {
    fun modifyItem(itemUID : String , isSnooz : Boolean)
    fun onItemDelete(itemUID: String)
}