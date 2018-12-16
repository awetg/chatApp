package com.awetdev.chatapp.model.network

import java.io.*

class SocketOutputHandler(val output: String) : Runnable {

    override fun run() {
        if (MySocket.connected) {
            val output = PrintWriter(MySocket.socket.getOutputStream(), true)

            try {
                output.print(this.output + "\r\n")
                if (output.checkError()) MySocket.connected = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}