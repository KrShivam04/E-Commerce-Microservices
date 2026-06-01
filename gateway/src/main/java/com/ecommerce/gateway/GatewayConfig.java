package com.ecommerce.gateway;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()

                // Order Service
                .route("order-service",
                        r -> r.path("/api/cart/**", "/api/order/**")
                                .uri("lb://order-service"))

                // Product Service
                .route("product-service",
                        r -> r.path("/api/products/**")
                                .uri("lb://product-service"))

                // User Service
                .route("user-service",
                        r -> r.path("/api/users/**")
                                .uri("lb://user-service"))

                // Eureka Dashboard Main Page
                .route("eureka-server",
                        r -> r.path("/eureka/main")
                                .filters(f -> f.setPath("/"))
                                .uri("http://localhost:8761"))

                // Eureka Static Resources
                .route("eureka-server-static",
                        r -> r.path("/eureka/**")
                                .uri("http://localhost:8761"))

                .build();
    }
}