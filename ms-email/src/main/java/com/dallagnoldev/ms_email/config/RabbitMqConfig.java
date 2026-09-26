package com.dallagnoldev.ms_email.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMqConfig {

    @Value(value = "${broker.queue.email.name}")
    private String queue;

    @Bean
    public Queue queue() {
        return new Queue(queue, true);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        JsonMapper jsonMapper = new JsonMapper();
        return new JacksonJsonMessageConverter(jsonMapper);
    }
}
