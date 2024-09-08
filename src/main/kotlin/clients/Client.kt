package clients

import base.Client
import utils.Constants
import java.io.*                  // استيراد الحزم اللازمة للتعامل مع الدخل والخرج.
import java.net.Socket             // استيراد Socket للتعامل مع اتصالات الشبكة.
import kotlin.concurrent.thread     // استيراد thread لتشغيل العمليات في خلفية العميل.

fun main() {
    val  socket = Socket("localhost", 9999)
    val client = Client(socket)
    client.start()
}
