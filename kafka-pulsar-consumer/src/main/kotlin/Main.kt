import org.apache.kafka.clients.consumer.KafkaConsumer
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

tailrec fun <T> repeatUntilSome(block: () -> T?): T = block() ?: repeatUntilSome(block)

fun main(args: Array<String>) {
    val consumerProps =
        mapOf(
            "bootstrap.servers" to "localhost:9092",
            "auto.offset.reset" to "earliest",
            "key.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
            "value.deserializer" to "org.apache.kafka.common.serialization.ByteArrayDeserializer",
            "group.id" to "someGroup",
            "security.protocol" to "PLAINTEXT"
        )

    KafkaConsumer<String, ByteArray>(consumerProps).use {
        it.subscribe(listOf("pulsar-testing-topic"))
        val message = repeatUntilSome {
            it.poll(400.milliseconds.toJavaDuration()).map { String(it.value()) }.firstOrNull()
        }
        println("MESSAGE CONSUMED $message")
    }
}