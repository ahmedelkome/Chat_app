package server

import utils.Constants
import java.net.ServerSocket
import kotlin.concurrent.thread

fun main() {
    val serverSocket = ServerSocket(Constants.PORT_NUMBER)  // create server socket
    println(Constants.SERVER_LISTENING)

    val clients = mutableListOf<ClientHandler>()  // list for store clients

    while (true) {
        val clientSocket = serverSocket.accept()  // accept client's connection
        println("New client connected: ${clientSocket.inetAddress}")

        val clientHandler = ClientHandler(clientSocket, clients)
        clients.add(clientHandler)

        // create new thread to work with clients in the same time without stop another thread
        thread {
            clientHandler.handleClient()
        }
    }
}


