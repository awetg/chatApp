package com.awetdev.chatapp.model.network

import android.util.Log
import com.awetdev.chatapp.model.AppRepository
import java.net.Socket

object MySocket {
    private val ip = "192.168.1.110"
    private val ipMobileHS = "192.168.43.206"
    private val ipEmulator = "10.0.2.2"
    private val port = 2323
    var socket = Socket()
    var connected = false

    fun connect() {
        Thread {
            try {

                MySocket.socket = Socket(ipEmulator, port)
                MySocket.connected = true
                AppRepository.getInstance().setConnected()
                Thread(ServerListener(MySocket.socket)).start()

            } catch (e: Exception) {
                AppRepository.getInstance().setConnectionError(e.message!!)
                e.printStackTrace()
                Log.e("MY LOG", "Socket connection error: ${e.message}")
            }
        }.start()
    }
}