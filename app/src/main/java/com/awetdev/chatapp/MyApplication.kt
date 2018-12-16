package com.awetdev.chatapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.awetdev.chatapp.model.database.ChatMessageDatabase
import com.awetdev.chatapp.model.network.MySocket
import com.awetdev.chatapp.model.network.SocketOutputHandler

//Application is extended for stopping background thread when app is killed
// It is used because it is the most convenient way to detect when app killed
class MyApplication : Application(), LifecycleObserver {


    private var chatMessageDatabase: ChatMessageDatabase? = null

    companion object {
        // having context inside static class creates memory leak.
        //it was used to provide context for a dedicated notification class, this context is used when message from server is received.
        //it would be possible to put the notification code inside a class which already have context(e.g service) but this approach was used for modularity.
        private var myContext: Context? = null

        val CHANNEL_1_ID = "channel1"

        fun getContext(): Context = myContext!!

        fun getNotificationChannel() = CHANNEL_1_ID
    }

    override fun onCreate() {
        super.onCreate()
        myContext = applicationContext
        //creating database instance require context, database is initialized at start of application as the classes using the db don't have context
        chatMessageDatabase = ChatMessageDatabase.getInstance(applicationContext)

        checkForUnreadMessage()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_1_ID, "messageChannel", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "This notification channel is for messages."
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeGround() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackGround() {
        if (MySocket.connected) {
            Thread(SocketOutputHandler(":quit")).start()
            MySocket.connected = false
        }
    }

    private fun checkForUnreadMessage() {
        Thread {
            val list = chatMessageDatabase?.chatMessageDao()?.getAllChatsWithUnread()
            if (list != null) {
                list.forEach {
                    User.listOfUnread.put(it, chatMessageDatabase?.chatMessageDao()?.getUnreadByChatId(it)?.size
                            ?: 0)
                }
            }
        }
    }
}