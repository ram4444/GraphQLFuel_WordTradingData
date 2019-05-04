package main.kotlin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import main.kotlin.handler.WSGraphQLHandler


@Configuration
@EnableWebFlux
@ComponentScan(value = ["org.zupzup.kotlinwebfluxdemo"])
class WebSocketConfig(
        val graphQLHandler: WSGraphQLHandler
) {
    @Bean
    fun websocketHandlerAdapter() = WebSocketHandlerAdapter()

    @Bean
    fun handlerMapping() : HandlerMapping {
        val handlerMapping = SimpleUrlHandlerMapping()
        handlerMapping.urlMap = mapOf(
                "/ws/graphql" to graphQLHandler
        )
        handlerMapping.order = 1
        return handlerMapping
    }
}