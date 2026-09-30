package com.dallagnoldev.ms_user.producer;

import com.dallagnoldev.ms_user.dto.EmailDto;
import com.dallagnoldev.ms_user.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.email.name}")
    private String emailRoutingKey;

    public void publishMessageEmail(UserModel user, String code) {
        EmailDto emailDto = EmailDto.builder()
                .emailTo(user.getEmail())
                .subject("Account Email Verification")
                .text("Your verification code is " + code + "\n The code is valid only for 15 minutes.")
                .build();

        rabbitTemplate.convertAndSend("",emailRoutingKey, emailDto);
    }
}
