package utils

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import models.ApplicationConfig

object ApplicationConfigService {
    /**
     * Reads in the configuration information from the provided application.yaml
     * @return the configuration object
     */
    fun getApplicationConfig(): ApplicationConfig {
        val configLocation = System.getenv("CONFIG_LOCATION") ?: "/application.yaml"
        return ConfigLoaderBuilder.default()
            .addResourceSource(configLocation)
            .build()
            .loadConfigOrThrow<ApplicationConfig>()
    }
}