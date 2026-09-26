package com.dallagnoldev.ms_email.consumer;

import com.dallagnoldev.ms_email.dto.EmailDto;
import com.dallagnoldev.ms_email.model.EmailModel;
import com.dallagnoldev.ms_email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailDto dto) {
        var emailModel = new EmailModel();
        BeanUtils.copyProperties(dto, emailModel);
        System.out.println(emailModel);
        emailService.sendEmail(emailModel);
    }
}
