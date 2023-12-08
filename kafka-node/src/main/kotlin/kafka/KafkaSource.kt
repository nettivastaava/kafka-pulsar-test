package kafka

import config.KafkaConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

data class KafkaSource(
    val sentIds:MutableList<String> = mutableListOf(),
    var totalSent: Int = 0,
    var totalAck: Int = 0,
) {
    fun start() {
        val kafkaProducer = KafkaProducer<String, ByteArray>(KafkaConfig.producerProps)
        val kafkaConsumer = KafkaConsumer<String, ByteArray>(KafkaConfig.consumerProps)
        println("Starting a Kafka source")
        while(true) {
            val generatedId = UUID.randomUUID().toString()

            kafkaProducer.send(ProducerRecord("kafka-testing-topic", generatedId.encodeToByteArray()))
            sentIds.add(generatedId)
            totalSent += 1

            kafkaConsumer.subscribe(listOf("kafka-confirmation-topic"))
            val records = kafkaConsumer.poll(400.milliseconds.toJavaDuration())
            for (record in records) {
                val consumedMessage = String(record.value())
                if (sentIds.contains(consumedMessage)) {
                    sentIds.remove(consumedMessage)
                    totalAck += 1
                    println("SENT: $totalSent, ACK: $totalAck")
                } else {
                    println("UNKNOWN ID FOUND")
                }
            }
            Thread.sleep(2000)
        }
    }
}