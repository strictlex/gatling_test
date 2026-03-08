package simulations

import java.io.OutputStreamWriter
import java.net.Socket


object `depricated GraphiteClient`{
    public fun pushGraphite(metricPath: String, value: Long) {
    val ts = System.currentTimeMillis() / 1000
    val line = "$metricPath $value $ts\n"
    Socket("localhost", 2003).use { sock ->
        OutputStreamWriter(sock.getOutputStream(), Charsets.UTF_8).use { w ->
            w.write(line)
            w.flush()
        }
    }
}}