import org.apache.pulsar.client.api.PulsarClient
import pulsar.PulsarDestination
import pulsar.PulsarSource
import utils.ApplicationConfigService

fun main() {
    val pulsarUrl = "pulsar://pulsar:6650"

    while (true) {
        try {
            val client = PulsarClient.builder()
                .serviceUrl(pulsarUrl)
                .build()

            val applicationConfig = ApplicationConfigService.getApplicationConfig()
            val type = applicationConfig.type

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