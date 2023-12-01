import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.PulsarClient

fun main() {
    val pulsarUrl = "pulsar://localhost:6650"

    val client = PulsarClient.builder()
        .serviceUrl(pulsarUrl)
        .build()

    val consumer: Consumer<ByteArray> = client.newConsumer()
        .topic("my-topic")
        .subscriptionName("my-subscription")
        .subscribe()

    val producer: Producer<ByteArray> = client.newProducer()
        .topic("confirmation-topic")
        .create()

    while (true) {
        val message = consumer.receive()
        println("Consumed message: ${String(message.data)}")
        consumer.acknowledge(message)

        producer.send(message.data)
        println("SENT BACK")
    }
}