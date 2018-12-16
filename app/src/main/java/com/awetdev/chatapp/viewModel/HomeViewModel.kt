package com.awetdev.chatapp.viewModel

import android.arch.lifecycle.*
import com.awetdev.chatapp.model.AppRepository
import com.awetdev.chatapp.model.database.ChatMessage

class HomeViewModel : ViewModel() {
    private val appRepository = AppRepository.getInstance()

    init {
        if (!appRepository.isConnected()) appRepository.connectToServer()
    }

    fun getLastMessages(): LiveData<List<ChatMessage>> = appRepository.getLastMessages()
    fun searchDB(queryText: String): LiveData<List<ChatMessage>> = appRepository.searchDB(queryText)
    fun getUnreadByChatId(chatId: Long): List<ChatMessage> = appRepository.getUnreadByChatId(chatId)

    val connected: LiveData<Boolean> = appRepository.getConnected()
    val getConnectionError: LiveData<Boolean> = appRepository.getConnectionError()
    val getLogInError: LiveData<Boolean> = appRepository.getLogInError()
    fun isConnected(): Boolean = appRepository.isConnected()
    fun logIn(user: String) = appRepository.logIn(user)
    fun logOut() = appRepository.logOut()
}