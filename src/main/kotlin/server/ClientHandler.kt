package server

import error.ErrorHandler
import utils.Constants
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ClientHandler(private val socket: Socket, private val clients: MutableList<ClientHandler>) {
    private val errorHandler = ErrorHandler()
    private val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
    private val writer = PrintWriter(socket.getOutputStream(), true)
    private var username: String? = null

    fun handleClient() {
        try {
             //read username form Client
            username = reader.readLine()
            if (username != null) {
                println("$username ${Constants.JOINED}")
                broadcast("$username ${Constants.JOINED}")

                var message: String?
                while (true) {
                    message = reader.readLine() ?: break
                    if (message.trim().equals("/quit", ignoreCase = true)) {
                        writer.println(Constants.YOU_HAVE_LEFT)
                        break
                    }

                    println("$username: $message")
                    broadcast("$username: $message")  //send message for all clients
                }
            }
        } catch (e: IOException) {
            errorHandler.handleDisconnection(username)  // Handle disconnection
        } finally {
            try {
                socket.close()  // Close connection with client
                errorHandler.handleSocketClosure(username = username)  // Handle socket closure
            } catch (e: IOException) {
                errorHandler.handleIOException(e)  // Handle IOException
            }
            synchronized(clients) {
                clients.remove(this)  // Remove client from list
            }
            username?.let {
                broadcast("$it has left the chat")
            }
        }
    }

    //broadcast all clients
    private fun broadcast(message: String) {
        synchronized(clients) {
            clients.forEach { client ->
                client.writer.println(message)
            }
        }
    }
}
