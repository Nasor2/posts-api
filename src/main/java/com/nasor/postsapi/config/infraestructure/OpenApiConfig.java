package com.nasor.postsapi.config.infraestructure;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Posts API",
                version = "1.0",
                description = "Posts and Users management API.",
                contact = @Contact(
                        name = "Samuel Peña Ortega",
                        email = "penaortegasamuel@gmail.com",
                        url = "https://portfolio-samuel-pos-projects.vercel.app/"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development"),
        }
)
public class OpenApiConfig {
}
