package com.davidarbana.secondhandclother.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /**
     * Custom OpenAPI configuration for Swagger UI.
     *
     * @return OpenAPI configuration with title, version, description, and contact info
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Second Hand Clothing API")
                        .version("1.0.0")
                        .description("API for managing second-hand clothing items")
                        .contact(new Contact()
                                .name("David Arbana")
                                .email("david_arbana@yahoo.com")
                                .url("https://exampleUrl.com")
                        )
                );
    }
}
