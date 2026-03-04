package simulations

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.*
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.io.OutputStreamWriter
import java.net.Socket
import java.time.Duration
import java.util.*
import java.util.concurrent.atomic.AtomicLong

class KafkaProducerSimulation : Simulation() {

    private val testDuration: Duration = Duration.ofSeconds(60)

    private val mapper = jacksonObjectMapper()

    private val producerProps = Properties().apply {
        put("bootstrap.servers", "localhost:9092")
        put("key.serializer", StringSerializer::class.java.name)
        put("value.serializer", StringSerializer::class.java.name)
        put("acks", "all")
        put("linger.ms", "5")
    }
    private val producer = KafkaProducer<String, String>(producerProps)


    private val okCount = AtomicLong(0)
    private val koCount = AtomicLong(0)
    private val lastLatencyMs = AtomicLong(0)


    private fun pushGraphite(metricPath: String, value: Long) {
        val ts = System.currentTimeMillis() / 1000
        val line = "$metricPath $value $ts\n"
        Socket("localhost", 2003).use { sock ->
            OutputStreamWriter(sock.getOutputStream(), Charsets.UTF_8).use { w ->
                w.write(line)
                w.flush()
            }
        }
    }

    private fun pushKafkaMetrics() {
        pushGraphite("gatling.kafka.send.ok", okCount.get())
        pushGraphite("gatling.kafka.send.ko", koCount.get())
        pushGraphite("gatling.kafka.send.latency_ms", lastLatencyMs.get())
    }

    private val kafkaScenario: ScenarioBuilder =
        scenario("Kafka load test")
            .exec { session ->
                val payload = mapOf(
                    "msg_id" to UUID.randomUUID().toString(),
                    "full_name" to Vars.randomFullName(),
                    "inn" to Vars.randomInn()
                )
                val json = mapper.writeValueAsString(payload)

                val start = System.nanoTime()
                try {
                    producer.send(ProducerRecord("test-topic", json)).get()
                    val latency = (System.nanoTime() - start) / 1_000_000
                    lastLatencyMs.set(latency)
                    okCount.incrementAndGet()
                } catch (e: Exception) {
                    koCount.incrementAndGet()
                }

                try { pushKafkaMetrics() } catch (_: Exception) {}

                session
            }

    init {
        setUp(
            kafkaScenario.injectOpen(
                constantUsersPerSec(10.0).during(testDuration),
                constantUsersPerSec(15.0).during(testDuration),
                constantUsersPerSec(25.0).during(testDuration),
                constantUsersPerSec(50.0).during(testDuration),
            )
        )

    }

    override fun after() {
        producer.close()
        try { pushKafkaMetrics() } catch (_: Exception) {}
    }
}