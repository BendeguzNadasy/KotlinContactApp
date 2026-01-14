package io.github.bendeguznadasy.contact.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun contactApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Contact CRUD API")
                    .description("REST API for managing contacts with image upload support (SQLite + Local Storage).")
                    .version("1.0.0")
                    .contact(
                        io.swagger.v3.oas.models.info.Contact()
                            .name("Bendegúz Nádasy")
                            .url("https://github.com/BendeguzNadasy")
                    )
            )
    }
}