package pulsar

import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.PulsarClient
import utils.ApplicationConfigService
import java.text.SimpleDateFormat
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

        val applicationConfig = ApplicationConfigService.getApplicationConfig()
        val timer1 = applicationConfig.timer1
        val timer2 = applicationConfig.timer2
        val count = applicationConfig.count
        var counter = count

        println("timer1 is: $timer1")
        println("timer2 is: $timer2")
        println("count is: $count")

        while(true) {

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            println("Time is: $currentDate")

            val generatedId = UUID.randomUUID().toString()
            producer.send(generatedId.toByteArray())
            sentIds.add(generatedId)
            totalSent += 1

            println("GeneratedId: $generatedId")

            val message = consumer.receive()
            val stringId = String(message.data)

            println("SentIds is: $sentIds")

            if (sentIds.contains(stringId)) {
                consumer.acknowledge(message)
                sentIds.remove(stringId)
                totalAck += 1
            } else {
                println("sendIds.contains(stringId) was NOT true")
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

            println("TOTAL SENT: $totalSent, TOTAL ACK: $totalAck, SENT-ACK: ${totalSent - totalAck}")
        }
    }
}