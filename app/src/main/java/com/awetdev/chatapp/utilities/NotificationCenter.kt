package com.awetdev.chatapp.utilities

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import com.awetdev.chatapp.MyApplication
import com.awetdev.chatapp.R
import com.awetdev.chatapp.User
import com.awetdev.chatapp.model.database.ChatMessage
import com.awetdev.chatapp.model.database.ChatMessageDatabase
import com.awetdev.chatapp.view.HomeActivity

class NotificationCenter(private val context: Context,private val chatMessage:ChatMessage) {

    private val chatMessageDao = ChatMessageDatabase.mInstance()?.chatMessageDao()
    private val channelId = MyApplication.getNotificationChannel()
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    operator fun invoke() {
        chatMessageDao?.insert(chatMessage)
        val notificationList = chatMessageDao?.getUnreadByChatId(chatMessage.chatId)
        User.listOfUnread.put(chatMessage.chatId,notificationList?.size!!)

        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra(context.resources.getString(R.string.CHAT_ID), chatMessage.chatId)
                .putExtra(context.resources.getString(R.string.CHAT_NAME), chatMessage.chatName)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(context, chatMessage.chatId.toInt(), intent, PendingIntent.FLAG_ONE_SHOT)

        val userProfilePhoto = BitmapFactory.decodeResource(context.resources, R.drawable.user_profile_dark)

        val messageStyle = NotificationCompat.MessagingStyle("Me")

        notificationList.forEach {
            messageStyle.addMessage(it.message, it.time, it.sender)
        }

        User.listOfUnread.put(chatMessage.chatId,notificationList.size?:0)

        if (chatMessage.chatName == "Group") messageStyle.conversationTitle = chatMessage.chatName


        val notification = NotificationCompat.Builder(context, channelId)
                .setContentTitle(chatMessage.sender)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(userProfilePhoto)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(messageStyle)
                .setAutoCancel(true)
                .build()
        notificationManager.notify(chatMessage.chatId.toInt(), notification)
    }
}