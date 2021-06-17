
// Required libraries

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.SocketException
import java.util.*

// Client creation

object Client2 {
    @Throws(SocketException::class)                  // If socket cannot be created
    @JvmStatic                                      // Required for object (?)
    fun main(args: Array<String>) {
        val c2Port = 8002
        var dpac: DatagramPacket
        val dsoc = DatagramSocket(c2Port)           // Specify client port
        val b = ByteArray(64)                  // Expected packet size
        var data: String
        val findCoords = """OP-2 COORDs: (\d+)\.(\d+)\.(\d+)""".toRegex()
        val findIP = """(\d+)\.(\d+)\.(\d+)\.(\d+)""".toRegex()
        println("Client is running...")

        // Packet transfer

        // Initializing with server...

        val clientAddress = Inet4Address.getLocalHost().toString()
        val foundAddress = findIP.find(clientAddress)?.value
        val opAdd1 = ("OP REQUEST: $foundAddress").toByteArray()
        dpac = DatagramPacket(opAdd1, opAdd1.size,                             // Message to send, bit size
            Inet4Address.getByName("localhost"), 8000)              // IPv4 and port number
        dsoc.send(dpac)

        try {
            while (true) {

                Thread.sleep(1000)          // Message delay

                // Messages to send

                val time = Date().toString()                             // Time stamp
                val messageTo1 = "OP-2 COORDs: 65.153.132 -- "          // Text message
                val mes1 = (messageTo1 + time).toByteArray()           // Total Message

                // Sending coordinates to server...

                dpac = DatagramPacket(mes1, mes1.size,                             // Message to send, bit size
                    Inet4Address.getByName("localhost"), 8000)          // IPv4 and port number
                dsoc.send(dpac)

                // Receiving server message...

                dpac = DatagramPacket(b, b.size)          // Expected packet size
                dsoc.receive(dpac)                       // Receives from server
                data = String(dpac.data)                // Convert pack to string & print

                if(!findCoords.containsMatchIn(data)) {         // Deletes duplicate coordinates
                    println("Received $data")
                }
            }

            // Exceptions & closing socket

        } catch (i: IOException) {
            println("Some exception...")
        }
        dsoc.close()
    }
}