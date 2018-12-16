package com.awetdev.chatapp.model.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

@Dao
interface ChatMessageDao {
    @Insert(onConflict = REPLACE)
    fun insert(notificationMessage: ChatMessage)

    @Query("SELECT * FROM chat_message WHERE chatId = :chatId AND read = :read")
    fun getUnreadByChatId(chatId: Long, read: Boolean = false): List<ChatMessage>

    @Query("SELECT chatId FROM chat_message WHERE read = :read")
    fun getAllChatsWithUnread(read: Boolean = false): List<Long>

    @Query("UPDATE chat_message SET read = :read WHERE chatId = :chatId")
    fun markReadByChatId(chatId: Long, read: Boolean = true)

    @Query("UPDATE chat_message SET read = :read WHERE chatName = :chatName")
    fun markReadByChatName(chatName: String, read: Boolean = true)

    @Query("SELECT * FROM chat_message WHERE id IN (SELECT MAX(id) FROM chat_message GROUP BY chatId) ORDER BY time DESC")
    fun getLastMessages(): LiveData<List<ChatMessage>>

    @Query("SELECT DISTINCT sender FROM chat_message WHERE NOT(sender = :exceptUser)")
    fun getUsers(exceptUser: String): LiveData<List<String>>

    @Query("SELECT * FROM chat_message WHERE chatId = :chatId")
    fun getMessagesByChatId(chatId: Long): LiveData<List<ChatMessage>>

    @Query("SELECT * FROM chat_message WHERE chatName = :chatName")
    fun getMessagesByChatName(chatName: String): LiveData<List<ChatMessage>>

    @Query("SELECT * FROM chat_message WHERE message LIKE :queryText OR sender LIKE :queryText")
    fun searchDB(queryText: String): LiveData<List<ChatMessage>>
}