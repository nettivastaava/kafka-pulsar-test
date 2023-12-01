package pulsar

import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.PulsarClient
import java.util.*

data class PulsarSource(
    val sentIds: MutableList<String> = mutableListOf(),
    var totalSent: Int = 0,
    var totalAck: Int = 0,
    val client: PulsarClient,
    val producer: Producer<ByteArray> = client.newProducer()
        .topic("my-topic")
        .create(),
    val consumer: Consumer<ByteArray> = client.newConsumer()
        .topic("confirmation-topic")
        .subscriptionName("my-confirmation")
        .subscribe()
) {
    fun start() {
        while(true) {
            val generatedId = UUID.randomUUID().toString()
            producer.send(generatedId.toByteArray())
            sentIds.add(generatedId)
            totalSent += 1

            println("SENT")

            val message = consumer.receive()

            consumer.acknowledge(message)
            val stringId = String(message.data)
            if (sentIds.contains(stringId)) {
                println("Consumed message: $stringId")
                sentIds.remove(stringId)
                totalAck += 1
            }

            println("TOTAL SENT: $totalSent, TOTAL ACK: $totalAck, SENT-ACK: ${totalSent - totalAck}")
        }
    }
}