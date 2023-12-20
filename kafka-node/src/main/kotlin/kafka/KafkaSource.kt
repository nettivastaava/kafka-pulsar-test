package kafka

import config.KafkaConfig
import models.ApplicationConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import utils.ApplicationConfigService
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

data class KafkaSource(
    val sentIds:MutableList<String> = mutableListOf(),
    var totalSent: Int = 0,
    var totalAck: Int = 0,
    val applicationConfig: ApplicationConfig = ApplicationConfigService.getApplicationConfig()
) {
    fun start() {
        println("[TESTAUS] producer start() called")
        val kafkaProducer = KafkaProducer<String, ByteArray>(KafkaConfig.producerProps)
        val kafkaConsumer = KafkaConsumer<String, ByteArray>(KafkaConfig.consumerProps)
        println("Starting a Kafka source")

        val timer1 = applicationConfig.timer1
        val timer2 = applicationConfig.timer2
        val count = applicationConfig.count
        val poll = applicationConfig.poll
        var counter = count

        kafkaConsumer.subscribe(listOf("kafka-confirmation-topic"))

        while(true) {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            println("Time is: $currentDate")

            val generatedId = UUID.randomUUID().toString()

            println("GeneratedId: $generatedId")
            kafkaProducer.send(ProducerRecord("kafka-testing-topic", generatedId.encodeToByteArray()))
            sentIds.add(generatedId)
            totalSent += 1

            val records = kafkaConsumer.poll(poll.milliseconds.toJavaDuration())
            for (record in records) {
                val consumedMessage = String(record.value())
                if (sentIds.contains(consumedMessage)) {
                    sentIds.remove(consumedMessage)
                    totalAck += 1
                    println("TOTAL SENT: $totalSent, TOTAL ACK: $totalAck, SENT-ACK: ${totalSent - totalAck}")
                } else {
                    println("UNKNOWN ID FOUND")
                }
            }

            counter--

            if (counter > 0) {
                println("sleep ${timer1.toString()} seconds")
                Thread.sleep(timer1.toLong())
            } else {
                println("sleep ${timer2.toString()} seconds")
                counter = count
                Thread.sleep(timer2.toLong())
            }
        }
    }
}