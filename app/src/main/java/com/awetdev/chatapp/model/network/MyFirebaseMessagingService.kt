package com.awetdev.chatapp.model.network


import android.content.Context
import com.awetdev.chatapp.R
import com.awetdev.chatapp.model.database.ChatMessage
import com.awetdev.chatapp.utilities.NotificationCenter
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val data: Map<String, String> = remoteMessage.data

        val user = data[this.resources.getString(R.string.SENDER)] ?: "Sender"
        val message = data[this.resources.getString(R.string.MESSAGE)] ?: "Message"
        val chatId = data[this.resources.getString(R.string.CHAT_ID)]?.toLong() ?: 0
        val chatName = data[this.resources.getString(R.string.CHAT_NAME)] ?: "Group"
        val time = data[this.resources.getString(R.string.TIME)]?.toLong() ?: System.currentTimeMillis()
        val read = data[this.resources.getString(R.string.READ)]?.toBoolean() ?: false

        NotificationCenter(this, ChatMessage(message, user, chatId, chatName, time, read)).invoke()
    }

    override fun onNewToken(token: String?) {
        val editor = getSharedPreferences(this.resources.getString(R.string.USER_DATA), Context.MODE_PRIVATE).edit()
        editor.putString(this.resources.getString(R.string.TOKEN), token)
        editor.apply()
    }
}