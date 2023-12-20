package kafka

import config.KafkaConfig.consumerProps
import config.KafkaConfig.producerProps
import models.ApplicationConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import utils.ApplicationConfigService
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class KafkaDestination(
    val applicationConfig: ApplicationConfig = ApplicationConfigService.getApplicationConfig()
) {
    fun start() {
        val poll = applicationConfig.poll
        println("[TESTAUS] consumer start() called")
        val kafkaProducer = KafkaProducer<String, ByteArray>(producerProps)
        val kafkaConsumer = KafkaConsumer<String, ByteArray>(consumerProps)
        println("Starting a Kafka destination")
        while (true) {
            kafkaConsumer.subscribe(listOf("kafka-testing-topic"))
            val records = kafkaConsumer.poll(poll.milliseconds.toJavaDuration())
            for (record in records) {
                val consumedMessage = String(record.value())
                println("MESSAGE CONSUMED $consumedMessage")

                kafkaProducer.send(ProducerRecord("kafka-confirmation-topic", consumedMessage.encodeToByteArray()))
                println("CONFIRMATION SENT FOR $consumedMessage")
            }
        }
    }
}