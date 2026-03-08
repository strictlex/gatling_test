package simulations

import io.gatling.javaapi.core.CoreDsl.constantUsersPerSec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Session
import io.gatling.javaapi.core.Simulation
import org.apache.kafka.clients.producer.ProducerConfig
import org.galaxio.gatling.kafka.javaapi.KafkaDsl.kafka
import java.time.Duration
import java.util.*


class KafkaProducerSimulation : Simulation() {

    private val testDuration: Duration = Duration.ofSeconds(60)

        private val kafkaConf = kafka()
        .topic("test-topic")
            .properties(mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
                ProducerConfig.ACKS_CONFIG to  "all",
                ProducerConfig.LINGER_MS_CONFIG to "5"))



    private val scn = scenario("Kafka load test")
        .exec { session: Session ->
            session
                .set("msgId", UUID.randomUUID().toString())
                .set("fullName", Vars.randomFullName())
                .set("inn", Vars.randomInn())
        }.exec(
            kafka("Send message")
                .send(
                    "#{msgId}",
                    """{"msg_id":"#{msgId}","full_name":"#{fullName}","inn":"#{inn}"}"""
                )
        )

    init {
        setUp(
            scn.injectOpen(
                constantUsersPerSec(10.0).during(testDuration),
                constantUsersPerSec(15.0).during(testDuration),
                constantUsersPerSec(25.0).during(testDuration),
                constantUsersPerSec(50.0).during(testDuration)
            )

        ).protocols(kafkaConf)

    }
}
