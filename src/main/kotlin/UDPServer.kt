import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import kotlin.jvm.Throws


object UDPServer {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        val datagramSocket = DatagramSocket(8000)
        val b = ByteArray(1024)
        var datagramPacket = DatagramPacket(b, 1024)
        println("Server is running...")
        println("OPs connected: 0")


        // Packet creation & transfer

        // Operator addresses
        val addresses = mutableSetOf<String>()
        val ports = mutableSetOf<String>()

        while (true) {

            // Receive:

            datagramSocket.receive(datagramPacket)
            val data = datagramPacket.data
            val dataString = String(data, 0, data.size)


            // Identifying operators:

            when {
                """OP REQUEST: """.toRegex()        // Identifying address of OP-1
                    .containsMatchIn(dataString) -> {
                    val opAdd = """(\d+)\.(\d+)\.(\d+)\.(\d+)""".toRegex().find(dataString)?.value
                    val opPort = """\d\d\d\d""".toRegex().find(dataString)?.value
                    ports.add(opPort.toString())
                    addresses.add(opAdd.toString())
                    println("OP FOUND @ $opAdd $opPort")
                }
            }

            // Transmitting coordinates...

            println("Sending data...")
            Thread.sleep(1000)          // Message delay

            // Packet transfer
            for(i in 0 until addresses.size ) {
                datagramPacket = DatagramPacket(
                    data,
                    data.size,
                    Inet4Address.getByName(addresses.elementAtOrNull(i)),
                    ports.elementAtOrNull(i)!!.toInt()
                )
                datagramSocket.send(datagramPacket)
                println(addresses.elementAtOrNull(i))
                println(ports.elementAtOrNull(i))
            }

            // Clearing buffer...

            datagramPacket = DatagramPacket(b, 1024)

        }
    }
}