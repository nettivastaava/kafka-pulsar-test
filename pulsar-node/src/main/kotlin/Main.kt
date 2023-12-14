import org.apache.pulsar.client.api.PulsarClient
import pulsar.PulsarDestination
import pulsar.PulsarSource
import utils.ApplicationConfigService

fun main() {
    var pulsarUrl = "pulsar://brokers-broker:6650"

    println("[TESTAUS] version is: 5")

    while (true) {
        try {
            val client = PulsarClient.builder()
                .serviceUrl(pulsarUrl)
                .build()

            val applicationConfig = ApplicationConfigService.getApplicationConfig()
            val type = applicationConfig.type

            println("[TESTAUS] type is: $type")

            if (type == "SOURCE") {
                val pulsarSource = PulsarSource(client = client)
                pulsarSource.start()
            } else {
                val pulsarDestination = PulsarDestination(client = client)
                pulsarDestination.start()
            }
        } catch (ex: Exception) {
            println("Failed due to $ex")
        }
    }
}