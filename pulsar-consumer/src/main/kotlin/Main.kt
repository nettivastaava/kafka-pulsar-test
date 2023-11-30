import org.apache.pulsar.client.api.Consumer
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

    while (true) {
        val message = consumer.receive()
        println("Consumed message: ${String(message.data)}")
        consumer.acknowledge(message)
    }


    client.close()
    consumer.close()
}