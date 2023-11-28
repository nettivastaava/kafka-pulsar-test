import config.KafkaConfig.consumerProps
import config.KafkaConfig.producerProps
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

fun main(args: Array<String>) {
    val kafkaProducer = KafkaProducer<String, ByteArray>(producerProps)
    val kafkaConsumer = KafkaConsumer<String, ByteArray>(consumerProps)


    while (true) {
        kafkaConsumer.subscribe(listOf("pulsar-testing-topic"))
        val records = kafkaConsumer.poll(400.milliseconds.toJavaDuration())
        for (record in records) {
            val consumedMessage = String(record.value())
            println("MESSAGE CONSUMED $consumedMessage")

            kafkaProducer.send(ProducerRecord("pulsar-confirmation-topic", "1", consumedMessage.encodeToByteArray()))
            println("CONFIRMATION SENT FOR $consumedMessage")
        }
    }
}