import kafka.KafkaDestination
import kafka.KafkaSource
import utils.ApplicationConfigService

fun main(args: Array<String>) {

    println("[TESTAUS] version is: 6")

    while (true) {
        try {
            val applicationConfig = ApplicationConfigService.getApplicationConfig()
            val type = applicationConfig.type

            if (type == "SOURCE") {
                val kafkaSource = KafkaSource()
                kafkaSource.start()
            } else {
                val kafkaDestination = KafkaDestination()

                kafkaDestination.start()
            }
        } catch (ex: Exception) {
            println("Failed due to $ex")
        }
    }
}