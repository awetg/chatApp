package com.awetdev.chatapp.model

import android.arch.lifecycle.LiveData
import com.awetdev.chatapp.User
import com.awetdev.chatapp.model.database.ChatMessageDatabase
import com.awetdev.chatapp.model.database.ChatMessage
import com.awetdev.chatapp.model.network.MySocket
import com.awetdev.chatapp.model.network.SocketOutputHandler

class ChatRepository {
    private val chatMessageDao = ChatMessageDatabase.mInstance()?.chatMessageDao()
    private var tempLiveData: LiveData<List<ChatMessage>> = chatMessageDao?.getMessagesByChatId(0)!!
    private var _accessedById = true

    companion object {
        @Volatile
        private var instance: ChatRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ChatRepository().also { instance = it }
        }
    }

    fun changeCurrentChat() {
        if (MySocket.connected) {
            if (User.accessChatById) Thread(SocketOutputHandler(":tochat ${User.currentChatId}")).start() else Thread(SocketOutputHandler(":to ${User.currentChat}")).start()
        }
    }

    fun sendMessage(message: String): Boolean {
        return if (MySocket.connected) {
            Thread(SocketOutputHandler(message)).start()
            true
        } else false
    }

    fun getMessages(): LiveData<List<ChatMessage>> {
        if (User.accessChatById)
            return chatMessageDao?.getMessagesByChatId(User.currentChatId.toLong())!!.also { tempLiveData = it; _accessedById = true }
        else
            return chatMessageDao?.getMessagesByChatName(User.currentChatName)!!.also { tempLiveData = it; _accessedById = false }
    }

    fun markReadByChatId(chatId: Long) = Thread { chatMessageDao?.markReadByChatId(chatId) }.start()

    fun markReadByChatName(chatName: String) = Thread { chatMessageDao?.markReadByChatName(chatName) }.start()

    fun chatObserverIsActive(chatId: String, chatName: String): Boolean {
        if (tempLiveData.hasActiveObservers()) {

            if (_accessedById) return chatId == User.currentChatId else return chatName == User.currentChatName

        } else return false
    }
}