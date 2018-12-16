package com.awetdev.chatapp.model.network

import android.util.Log
import java.net.Socket
import java.util.*

class ServerListener(socket: Socket) : Runnable {
    val scanner = Scanner(socket.getInputStream()).useDelimiter(System.lineSeparator())
    val inputList = mutableListOf<String>()
    val _EOS = Regex("^EOS:\\S+$")
    val _flaggedOutput = Regex("^\\S+:")
    var endOfStream = false
    var temp = listOf<String>()

    override fun run() {
        while (MySocket.connected) {
            try {
                while (!endOfStream) {

                    val message = scanner.nextLine()

                    if (message.contains(_EOS)) {
                        endOfStream = true
                    } else inputList.add(message)
                }
                if (inputList.size > 0) {
                    temp = inputList.toList() //copying the list

                    //server output handler thread, thread have different responsibility, SRP not followed for shorter code and fewer classes
                    Thread {
                        val type = _flaggedOutput.find(temp.component1())?.value
                        if (type != null) messageParsersList.get(type)?.invoke(temp)
                    }.start()
                }
                inputList.clear()
                endOfStream = false

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MY LOG", "Server Listener error message: ${e.message}")
                MySocket.connected = false
            }
        }
    }

}

internal val messageParsersList = mapOf(
        "ERROR:" to errorParser,
        "CONNECTED:" to serverConnected,
        "LOGGED:" to setLoggedIn,
        "USERS:" to users,
        "CHAT_ID:" to messages,
        "CHAT_NAME:" to lastMessages,
        "CHAT_CHANGED:" to chatChanged
)