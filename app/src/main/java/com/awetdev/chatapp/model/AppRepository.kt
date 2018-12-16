package com.awetdev.chatapp.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.awetdev.chatapp.model.database.ChatMessage
import com.awetdev.chatapp.model.database.ChatMessageDatabase
import com.awetdev.chatapp.model.network.MySocket
import com.awetdev.chatapp.model.network.SocketOutputHandler

class AppRepository {

    private val chatMessageDao = ChatMessageDatabase.mInstance()?.chatMessageDao()

    private val logged = MutableLiveData<Boolean>()
    private val connected = MutableLiveData<Boolean>()
    private val connectionError = MutableLiveData<Boolean>()
    private val signUpError = MutableLiveData<Boolean>()
    private val logInError = MutableLiveData<Boolean>()

    companion object {
        @Volatile
        private var instance: AppRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance
                    ?: AppRepository().also { instance = it }
        }
    }

    fun getLastMessages(): LiveData<List<ChatMessage>> = chatMessageDao?.getLastMessages()!!

    fun searchDB(queryText: String): LiveData<List<ChatMessage>> = chatMessageDao?.searchDB(queryText)!!

    fun getUnreadByChatId(chatId: Long): List<ChatMessage> {
        var list = listOf<ChatMessage>()
        Thread { list = chatMessageDao?.getUnreadByChatId(chatId)!! }
        return list
    }

    fun setConnected() = connected.postValue(true)
    fun getConnected() = connected
    fun isConnected() = MySocket.connected

    fun setLoggedIn(value: Boolean) = logged.postValue(value)
    fun getLoggedIn(): LiveData<Boolean> = logged

    fun setSignUpError() = signUpError.postValue(true)
    fun getSignUpError(): LiveData<Boolean> = signUpError

    fun setLogInError() = logInError.postValue(true)
    fun getLogInError(): LiveData<Boolean> = logInError

    fun setConnectionError(error: String = "") = connectionError.postValue(true)
    fun getConnectionError(): LiveData<Boolean> = connectionError

    //server communication methods ------
    fun connectToServer() = MySocket.connect()

    fun setUserName(username: String) = Thread(SocketOutputHandler(":user $username")).start()

    fun addToken(token: String) = Thread(SocketOutputHandler(":addtoken $token")).start()

    fun logIn(username: String) = Thread(SocketOutputHandler(":login $username")).start()

    fun logOut() = {
        Thread(SocketOutputHandler(":logout")).start()
        AppRepository.getInstance().setLoggedIn(false)
    }
}