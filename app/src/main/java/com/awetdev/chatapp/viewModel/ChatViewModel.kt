package com.awetdev.chatapp.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.awetdev.chatapp.model.AppRepository
import com.awetdev.chatapp.model.ChatRepository
import com.awetdev.chatapp.model.database.ChatMessage

class ChatViewModel: ViewModel() {
    private val chatRepository = ChatRepository.getInstance()
    private val appRepository = AppRepository.getInstance()

    init {chatRepository.changeCurrentChat()}

    fun getMessageById():LiveData<List<ChatMessage>> = chatRepository.getMessages()
    fun changeCurrentChat() = chatRepository.changeCurrentChat()
    fun sendMessage(message: String):Boolean = chatRepository.sendMessage(message)
    fun markReadByChatId(chatId:Long) = chatRepository.markReadByChatId(chatId)
    fun markReadByChatName(chatName:String) = chatRepository.markReadByChatName(chatName)

    val connected:LiveData<Boolean> = appRepository.getConnected()
    val getConnectionError:LiveData<Boolean> = appRepository.getConnectionError()
    val getLogInError: LiveData<Boolean> = appRepository.getLogInError()
    fun connect() = appRepository.connectToServer()
    fun logIn(user: String) = appRepository.logIn(user)
}