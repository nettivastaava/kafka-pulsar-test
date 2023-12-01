import org.apache.pulsar.client.api.PulsarClient
import io.github.cdimascio.dotenv.dotenv
import pulsar.PulsarDestination
import pulsar.PulsarSource

fun main() {
    val pulsarUrl = "pulsar://localhost:6650"

    val client = PulsarClient.builder()
        .serviceUrl(pulsarUrl)
        .build()

    val dotenv = dotenv {
        filename = ".env"
    }

    val type = dotenv["TYPE"]

    if (type == "SOURCE") {
        val pulsarSource = PulsarSource(client = client)

        pulsarSource.start()
    } else {
        val pulsarDestination = PulsarDestination(client = client)

        pulsarDestination.start()
    }
}