package com.controlcash.app.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        License license = new License()
                .name("MIT")
                .url("https://github.com/Joao-Darwin/controlcash-api/blob/main/LICENSE");

        Contact contact = new Contact()
                .name("João Darwin")
                .email("joaodarwin.ip22@gmail.com")
                .url("https://joaodarwin.dev");

        Info info = new Info()
                .title("Control Cash API")
                .description("API from app Control Cash")
                .summary("API allows you manager your transactions, like payment or entrance. You can separate by categories or create goals for your money")
                .version("1.0.0")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info);
    }
}
