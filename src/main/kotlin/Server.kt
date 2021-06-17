
// Required libraries

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.SocketException

// Server creation

object Server {
    @Throws(SocketException::class)          // If socket cannot be created
    @JvmStatic                              // Required for object (?)
    fun main(args: Array<String>) {
        var dpac: DatagramPacket                  // Definitions & Server status
        val dsoc = DatagramSocket(8000)     // Specify server port
        val b = ByteArray(64)              // Expected packet size
        var data: String
        val findIP = """(\d+)\.(\d+)\.(\d+)\.(\d+)""".toRegex()
        println("Server is running...")

        // Potential Connections:

//        var opAdd1 = 0
//        val opAdd2 = 0
//        val opAdd3 = 0

        // Packet creation & transfer

        try {
            while (true) {

                // Receiving messages...

                dpac = DatagramPacket(b, b.size)          // Expected packet size
                dsoc.receive(dpac)                       // Receives from server
                data = String(dpac.data)                // Convert pack to string & print

                // Checking for operators...

                when {
                    """OP-1 IP: """.toRegex()          // Identifying address of OP-1
                        .containsMatchIn(data) -> {
                        val opAdd1 = findIP.find(data)?.value
                        println("OP-1 FOUND @ $opAdd1")
                    }
                    """OP-2 IP: """.toRegex()          // Identifying address of OP-2
                        .containsMatchIn(data) -> {
                        val opAdd1 = findIP.find(data)?.value
                        println("OP-2 FOUND @ $opAdd1")
                    }
                    """OP-3 IP: """.toRegex()          // Identifying address of OP-3
                        .containsMatchIn(data) -> {
                        val opAdd1 = findIP.find(data)?.value
                        println("OP-3 FOUND @ $opAdd1")
                    }
                }

                // Transmitting coordinates...

                println("Sending data...")
                val datapac = data.toByteArray()
                Thread.sleep(1000)          // Message delay

                // Packet transfer

                dpac = DatagramPacket(datapac, datapac.size,                             // Sending data to Client1
                    Inet4Address.getByName("localhost"), 8001)
                dsoc.send(dpac)
                dpac = DatagramPacket(datapac, datapac.size,                             // Sending data to Client2
                    Inet4Address.getByName("localhost"), 8002)
                dsoc.send(dpac)
                dpac = DatagramPacket(datapac, datapac.size,                             // Sending data to Client3
                    Inet4Address.getByName("localhost"), 8003)
                dsoc.send(dpac)
            }

            // Exceptions & closing socket

        } catch (e: IOException) {
            println("IO Exception")
        } catch (e: InterruptedException) {
            println("IO Exception")
        }
        dsoc.close()
    }
}