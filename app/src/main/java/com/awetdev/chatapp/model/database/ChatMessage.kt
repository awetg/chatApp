package com.awetdev.chatapp.model.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "chat_message")
data class ChatMessage(
        var message: String,
        var sender: String,
        var chatId: Long,
        var chatName: String,
        var time: Long = System.currentTimeMillis(),
        var read: Boolean = true,
        @PrimaryKey(autoGenerate = true) var id: Int = 0)