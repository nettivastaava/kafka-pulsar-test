package pulsar

import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.PulsarClient

data class PulsarDestination(
    val client: PulsarClient,
    val producer: Producer<ByteArray> = client.newProducer()
        .topic("confirmation-topic")
        .create(),
    val consumer: Consumer<ByteArray> = client.newConsumer()
        .topic("my-topic")
        .subscriptionName("my-subscription")
        .subscribe()
) {
    fun start() {
        println("[TESTAUS] consumer start() called")
        while (true) {
            val message = consumer.receive()
            println("Consumed message: ${String(message.data)}")
            consumer.acknowledge(message)

            producer.send(message.data)
            println("SENT BACK")
        }
    }
}