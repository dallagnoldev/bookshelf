package com.dallagnoldev.ms_books.config;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMqConfig {

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        JsonMapper jsonMapper = new JsonMapper();
        return new JacksonJsonMessageConverter(jsonMapper);
    }
}
