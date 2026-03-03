package com.example

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.*
import org.galaxio.gatling.kafka.javaapi.KafkaDsl.*
import org.apache.kafka.clients.producer.ProducerConfig
import java.time.Duration
import java.util.UUID


class KafkaLoadSimulation : Simulation(){
    private val kafkaConf = kafka()
        .properties(
            mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
                ProducerConfig.ACKS_CONFIG to "1"
            )
        )


    val scn = scenario("Kafka Load Test")
        .exec { session : Session ->
            session
                .set("msgId", UUID.randomUUID().toString())
                .set("fullName", Vars.randomFullName())
                .set("inn", Vars.randomInn())
        }.exec (
            kafka("Send message").topic("test-topic")
                .send("#{msgId}",
                    """{"msg_id":"#{msgId}","full_name":"#{fullName}","inn":"#{inn}"}""".trimMargin()

                )
            )


    init {
        setUp(
            scn.injectOpen(

                constantUsersPerSec(5.0).during(Duration.ofMinutes(2)),
                constantUsersPerSec(10.0).during(Duration.ofMinutes(2)),
                constantUsersPerSec(25.0).during(Duration.ofMinutes(2)),
                constantUsersPerSec(50.0).during(Duration.ofMinutes(2))
            )
        ).protocols(kafkaConf)
    }
}