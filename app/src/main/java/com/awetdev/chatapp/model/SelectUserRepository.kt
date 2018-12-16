package com.awetdev.chatapp.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.awetdev.chatapp.User
import com.awetdev.chatapp.model.database.ChatMessageDatabase
import com.awetdev.chatapp.model.network.MySocket
import com.awetdev.chatapp.model.network.SocketOutputHandler

class SelectUserRepository {
    private val userList = MutableLiveData<List<String>>()
    private val temp = mutableListOf<String>()

    private val notificationMessageDao = ChatMessageDatabase.mInstance()?.chatMessageDao()


    companion object {
        @Volatile
        private var instance: SelectUserRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: SelectUserRepository().also { instance = it }
        }
    }

    fun loadUsers() = Thread(SocketOutputHandler(":users")).start()

    fun getUserList(): LiveData<List<String>> = if (MySocket.connected) userList else notificationMessageDao?.getUsers(User.name)!!

    //from network if app is connected
    fun insertUser(users: List<String>) {
        val diff = users.minus(temp)
        if (diff.isNotEmpty()) {
            temp.addAll(diff)
            userList.postValue(temp)
        }
    }
}