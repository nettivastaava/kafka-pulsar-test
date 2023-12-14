package kafka

import config.KafkaConfig.consumerProps
import config.KafkaConfig.producerProps
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class KafkaDestination() {
    fun start() {
        println("[TESTAUS] consumer start() called")
        val kafkaProducer = KafkaProducer<String, ByteArray>(producerProps)
        val kafkaConsumer = KafkaConsumer<String, ByteArray>(consumerProps)
        println("Starting a Kafka destination")
        while (true) {
            kafkaConsumer.subscribe(listOf("kafka-testing-topic"))
            val records = kafkaConsumer.poll(400.milliseconds.toJavaDuration())
            for (record in records) {
                val consumedMessage = String(record.value())
                println("MESSAGE CONSUMED $consumedMessage")

                kafkaProducer.send(ProducerRecord("kafka-confirmation-topic", consumedMessage.encodeToByteArray()))
                println("CONFIRMATION SENT FOR $consumedMessage")
            }
        }
    }
}