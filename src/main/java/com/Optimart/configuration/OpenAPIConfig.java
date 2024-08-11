package com.Optimart.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@SecurityScheme(
        name = "bearerAuth", // Đổi tên này thành "bearerAuth" để khớp với @SecurityRequirement
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenAPIConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                Server devServer = new Server();
                devServer.setUrl("http://localhost:8080");
                devServer.setDescription("Server URL in Development environment");

                Contact contact = new Contact();
                contact.setEmail("phamnguyentu04@gmail.com");
                contact.setName("Pham Nguyen Tu");
                contact.setUrl("https://www.linkedin.com/in/ngntu10/");

                License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

                Info info = new Info()
                        .title("OPTIMART API")
                        .version("1.0.1")
                        .contact(contact)
                        .description("This API exposes optimart endpoints.")
                        .termsOfService("https://www.linkedin.com/in/ngntu10/")
                        .license(mitLicense);

                return new OpenAPI()
                        .info(info)
                        .servers(List.of(devServer)); // Áp dụng security scheme cho toàn bộ API
        }
}
