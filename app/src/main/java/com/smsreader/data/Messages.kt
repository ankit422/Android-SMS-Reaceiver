package com.smsreader.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//@Entity(tableName = "messages")
//data class Messages(
//    @PrimaryKey(autoGenerate = true) val uid: Int,
//    var message: String?,
//    var cost: String?,
//    var date: String?,
//    var created: Date?
//)

@Entity(tableName = "messages")
class Messages {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
    var message: String? = null
    var cost: String? = null
    var date: String? = null
    var created: Date? = Date(System.currentTimeMillis())
}
