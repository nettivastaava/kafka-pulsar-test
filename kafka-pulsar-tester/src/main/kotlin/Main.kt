import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <K, V> Producer<K, V>.asyncSend(record: ProducerRecord<K, V>) =
    suspendCoroutine<RecordMetadata> { continuation ->
        send(record) { metadata, exception ->
            exception?.let(continuation::resumeWithException)
                ?: continuation.resume(metadata)
        }
    }

fun main(args: Array<String>) {
    val producerProps = mapOf<String, Any>(
        "bootstrap.servers" to "localhost:9092",
        "key.serializer" to "org.apache.kafka.common.serialization.StringSerializer",
        "value.serializer" to "org.apache.kafka.common.serialization.ByteArraySerializer",
        "security.protocol" to "PLAINTEXT"
    )

    val kafkaProducer = KafkaProducer<String, ByteArray>(producerProps)

    kafkaProducer.use {
        it.send(ProducerRecord("pulsar-testing-topic", "1", "Hello, world!".encodeToByteArray())) // Or asyncSend
    }
}