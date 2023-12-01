import config.KafkaConfig.consumerProps
import config.KafkaConfig.producerProps
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

fun main(args: Array<String>) {
    val sentIds: MutableList<String> = mutableListOf()
    var totalSent = 0
    var totalAck = 0
    val kafkaProducer = KafkaProducer<String, ByteArray>(producerProps)
    val kafkaConsumer = KafkaConsumer<String, ByteArray>(consumerProps)

    while (true) {
        val generatedId = UUID.randomUUID().toString()

        kafkaProducer.send(ProducerRecord("pulsar-testing-topic", generatedId.encodeToByteArray()))
        sentIds.add(generatedId)
        totalSent += 1

        kafkaConsumer.subscribe(listOf("pulsar-confirmation-topic"))
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