import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import javax.sound.sampled.*
import kotlin.jvm.Throws

object Sender {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F, 16, 2, 4, 44100F, true)
        var microphone: TargetDataLine
        val speakers: SourceDataLine
        try {
            microphone = AudioSystem.getTargetDataLine(format)
            val info = DataLine.Info(TargetDataLine::class.java, format)
            microphone = AudioSystem.getLine(info) as TargetDataLine
            microphone.open(format)
            val out = ByteArrayOutputStream()
            var numBytesRead: Int
            val CHUNK_SIZE = 1024
            val data = ByteArray(microphone.bufferSize / 5)
            microphone.start()
            val dataLineInfo = DataLine.Info(SourceDataLine::class.java, format)
            speakers = AudioSystem.getLine(dataLineInfo) as SourceDataLine
            speakers.open(format)
            speakers.start()


            // Configure the ip and port
            val hostname = "localhost"
            val port = 5555
            val address = InetAddress.getByName(hostname)
            val socket = DatagramSocket()
            val buffer = ByteArray(1024)
            while (true) {
                numBytesRead = microphone.read(data, 0, CHUNK_SIZE)
                //  bytesRead += numBytesRead;
                // write the mic data to a stream for use later
                out.write(data, 0, numBytesRead)
                // write mic data to stream for immediate playback
                speakers.write(data, 0, numBytesRead)
                val request = DatagramPacket(data, numBytesRead, address, port)
                socket.send(request)
            }
        } catch (e: LineUnavailableException) {
            e.printStackTrace()
        }
    }
}