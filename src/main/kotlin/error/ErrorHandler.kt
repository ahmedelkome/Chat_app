package error

import utils.Constants
import java.io.IOException

class ErrorHandler {

    fun handleDisconnection(username: String?) {
        if (username != null) {
            println("$username ${Constants.CLIENT_DISCONNECTED}")
        }
    }

    fun handleIOException(e: IOException) {
        println("IOException occurred: ${e.localizedMessage}")
    }

    fun handleSocketClosure(username: String?) {
        println("Connection whit $username closed successfully.")
    }

}
