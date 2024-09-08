package base

import error.ErrorHandler
import utils.Constants
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class Client(private val socket: Socket) {
    private val errorHandler = ErrorHandler()
    private val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
    private val writer = PrintWriter(socket.getOutputStream(), true)
    private var running = true

    fun start() {
        thread { getMessagesFromServer() }
        getMessagesFromClient()
        closeSocket()
    }

    private fun getMessagesFromServer() {
        println(Constants.ENTER_YOUR_USER_NAME)
        while (running) {
            try {
                val message = reader.readLine()
                if (message != null && message.isNotEmpty()) {
                    println(message)  //read message from server
                }
            } catch (e: IOException) {
                errorHandler.handleIOException(e)
                running = false
            }
        }
    }

    private fun getMessagesFromClient() {
        while (running) {
            val message = readLine()
            if (!message.isNullOrEmpty()) {
                if (message.trim().equals("/quit", ignoreCase = true)) {
                    writer.println("/quit")
                    running = false
                } else {
                    writer.println(message)  // send message for server
                }
            }else{
                println("Message can't be empty")
            }
        }
    }

    private fun closeSocket() {
        try {
            socket.close()
        } catch (e: IOException) {
            errorHandler.handleIOException(e)
        }
    }
}
