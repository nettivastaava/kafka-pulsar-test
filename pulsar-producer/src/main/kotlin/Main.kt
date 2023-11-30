import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.PulsarClient

fun main() {
    val pulsarUrl = "pulsar://localhost:6650"

    val client = PulsarClient.builder()
        .serviceUrl(pulsarUrl)
        .build()

    val producer: Producer<ByteArray> = client.newProducer()
        .topic("my-topic")
        .create()

    val message = "ABC 123"

    producer.send(message.toByteArray())

    producer.close()
    client.close()
}