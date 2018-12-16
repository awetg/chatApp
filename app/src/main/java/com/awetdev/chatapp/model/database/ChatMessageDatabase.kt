package com.awetdev.chatapp.model.database

import android.arch.persistence.room.*
import android.content.Context

@Database(entities = [ChatMessage::class], version = 1)
abstract class ChatMessageDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        private var instance: ChatMessageDatabase? = null

        fun getInstance(context: Context): ChatMessageDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(context, ChatMessageDatabase::class.java, "chat_message.db").build()
                }
            }
            return instance!!
        }

        fun mInstance(): ChatMessageDatabase? = instance
    }
}