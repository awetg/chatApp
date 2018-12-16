package com.awetdev.chatapp.model.network

import android.util.Log
import com.awetdev.chatapp.MyApplication
import com.awetdev.chatapp.User
import com.awetdev.chatapp.model.*
import com.awetdev.chatapp.model.database.ChatMessageDatabase
import com.awetdev.chatapp.model.database.ChatMessage
import com.awetdev.chatapp.utilities.NotificationCenter
import java.lang.Exception

val _chatIdRegex = Regex("CHAT_ID:(\\S+)")
val _chatNameRegex = Regex("CHAT_NAME:(\\S+)")
val _senderRegex = Regex("FROM:(\\S+)")
val _timeRegex = Regex("TIME:(\\S+)")
val _messageRegex = Regex("MESSAGE:(.*)")


val serverConnected = { _: List<String> -> }

val setLoggedIn = { _: List<String> ->
    AppRepository.getInstance().setLoggedIn(true)
    User.logged = true
}

val users = { inputList: List<String> ->
    val users = inputList.component1().replace("USERS:", "").trim().split(" ").toMutableList()
    users.remove(User.name)
    SelectUserRepository.getInstance().insertUser(users)
}

val messages = { inputList: List<String> ->
    try {
        inputList.forEach {
            val sender = _senderRegex.find(it)!!.groupValues[1]
            val message = _messageRegex.find(it)!!.groupValues[1]
            val time = _timeRegex.find(it)!!.groupValues[1]
            val chatId = _chatIdRegex.find(it)!!.groupValues[1]
            val chatName = _chatNameRegex.find(it)!!.groupValues[1]

            if (ChatRepository.getInstance().chatObserverIsActive(chatId, chatName)) {
                val dao = ChatMessageDatabase.mInstance()?.chatMessageDao()
                dao?.insert(ChatMessage(message, sender, chatId.toLong(), chatName, time.toLong(), true))

            } else
                NotificationCenter(MyApplication.getContext(), ChatMessage(message, sender, chatId.toLong(), chatName, time.toLong(), false)).invoke()

        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("MY LOG", "error: ${e.message}")
    }
}


val errorParser = { inputList: List<String> ->
    val input = inputList.component1()
    if (input.endsWith("USERNAME_TAKEN")) {
        AppRepository.getInstance().setSignUpError()
    } else if (input.endsWith("USER_NOT_FOUND") || input.endsWith("SYNTAX_ERROR")) {
        AppRepository.getInstance().setLogInError()
    } else if (input.endsWith("USER_ALREADY_LOGGED")) {

    }
}


//Not used after data persistence implemented

val chatChanged = { _: List<String> ->
    //    val chatId = _chatIdRegex.find(inputList.component1())!!.groupValues[1]
//    User.currentChatId = chatId
}

val lastMessages = { _: List<String> ->
    //    val lastMessages = mutableListOf<LastMessage>()
//    inputList.forEach {
//        if(it.isNotEmpty()) {
//            var chatName = _chatNameRegex.find(it)!!.groupValues[1]
//            val chatId = _chatIdRegex.find(it)!!.groupValues[1]
//            val user = _senderRegex.find(it)!!.groupValues[1]
//            chatName = if(chatName == "Group") chatName else chatName.replace(User.name,"").replace(",","")
//            val time = _timeRegex.find(it)!!.groupValues[1]
//            val message = _messageRegex.find(it)!!.groupValues[1]
//            lastMessages.add(LastMessage(chatName, chatId, user, message, time))
//        }
//    }
//    HomeRepository.getInstance().insertLastMessages(lastMessages)
}