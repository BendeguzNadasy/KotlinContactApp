package io.github.bendeguznadasy.contact.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // Minden végpontra érvényes
            .allowedOrigins("http://localhost:5173") // Csak a Frontend portját engedjük
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Engedélyezett metódusok
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}