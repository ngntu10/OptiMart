package com.Optimart.config;

import java.util.List;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@SecurityScheme(
        name = "bearer-key",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenAPIConfig {

//        @Value("${optimart.openapi.dev-url}")
//        private String devUrl;
//
//        @Value("${optimart.openapi.prod-url}")
//        private String prodUrl;

        @Bean
        public OpenAPI myOpenAPI() {
                Server devServer = new Server();
                devServer.setUrl("http://localhost:8080");
                devServer.setDescription("Server URL in Development environment");

//                Server prodServer = new Server();
//                prodServer.setUrl(prodUrl);
//                prodServer.setDescription("Server URL in Production environment");

                Contact contact = new Contact();
                contact.setEmail("phamnguyentu04@gmail.com");
                contact.setName("Pham Nguyen Tu");
                contact.setUrl("https://www.linkedin.com/in/ngntu10/");

                License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

                Info info = new Info()
                        .title("OPTIMART API")
                        .version("1.0.1")
                        .contact(contact)
                        .description("This API exposes endpoints to manage optimart.").termsOfService("https://www.linkedin.com/in/ngntu10/")
                        .license(mitLicense);

                return new OpenAPI().info(info).servers(List.of(devServer));
        }
}


