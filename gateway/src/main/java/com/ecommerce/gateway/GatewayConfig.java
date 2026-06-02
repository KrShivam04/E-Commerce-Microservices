package com.ecommerce.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 20, 1);
    }

    @Bean
    public KeyResolver hostNameKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress()
        );
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()

                // Order Service
                .route("order-service",
                        r -> r.path("/api/cart/**", "/api/order/**")
                                .filters(f -> f
                                        .requestRateLimiter(config -> config
                                                .setRateLimiter(redisRateLimiter())
                                                .setKeyResolver(hostNameKeyResolver()))
                                        .circuitBreaker(config -> config
                                                .setName("orderService")
                                                .setFallbackUri("forward:/fallback/orders")))
                                .uri("lb://order-service"))

                // Product Service
                .route("product-service",
                        r -> r.path("/api/products/**")
                                .filters(f -> f
                                        .requestRateLimiter(config -> config
                                                .setRateLimiter(redisRateLimiter())
                                                .setKeyResolver(hostNameKeyResolver()))
                                        .circuitBreaker(config -> config
                                                .setName("productBreaker")
                                                .setFallbackUri("forward:/fallback/products")))
                                .uri("lb://product-service"))

                // User Service
                .route("user-service",
                        r -> r.path("/api/users/**")
                                .filters(f -> f
                                        .requestRateLimiter(config -> config
                                                .setRateLimiter(redisRateLimiter())
                                                .setKeyResolver(hostNameKeyResolver()))
                                        .circuitBreaker(config -> config
                                                .setName("userService")
                                                .setFallbackUri("forward:/fallback/users")))
                                .uri("lb://user-service"))

                // Eureka Dashboard
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