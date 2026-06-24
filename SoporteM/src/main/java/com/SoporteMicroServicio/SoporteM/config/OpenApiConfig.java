package com.SoporteMicroServicio.SoporteM.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Microservicio Soporte - EcoMarket",
        version = "1.0",
        description = "API REST para la gestión de tickets, reclamos y solicitudes de soporte del sistema EcoMarket",
        contact = @Contact(name = "Equipo Soporte", email = "soporte@ecomarket.cl")
    ),
    servers = @Server(url = "http://localhost:8081", description = "Servidor local")
)
public class OpenApiConfig {

}
