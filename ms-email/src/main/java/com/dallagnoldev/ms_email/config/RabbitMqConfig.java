package com.dallagnoldev.ms_email.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMqConfig {

    @Value(value = "${broker.queue.email.name}")
    private String emailQueue;

    @Value(value = "${broker.queue.loan.name}")
    private String loanQueue;

    @Bean
    public Queue verificationEmailQueue() {
        return new Queue(emailQueue, true);
    }

    @Bean
    public Queue loanEmailQueue() {
        return new Queue(loanQueue, true);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        JsonMapper jsonMapper = new JsonMapper();
        return new JacksonJsonMessageConverter(jsonMapper);
    }
}