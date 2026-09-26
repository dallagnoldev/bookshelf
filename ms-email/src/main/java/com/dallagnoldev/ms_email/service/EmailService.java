package com.dallagnoldev.ms_email.service;

import com.dallagnoldev.ms_email.dto.EmailDto;
import com.dallagnoldev.ms_email.enums.EmailStatus;
import com.dallagnoldev.ms_email.model.EmailModel;
import com.dallagnoldev.ms_email.repository.IEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final IEmailRepository emailRepository;
    private final JavaMailSender mailSender;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    @Transactional
    public void sendEmail(EmailModel emailModel) {
        try {
            emailModel.setSendDateEmail(LocalDateTime.now());
            emailModel.setEmailFrom(emailFrom);

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(emailFrom);
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());

            mailSender.send(message);

            emailModel.setStatus(EmailStatus.SENT);
        } catch (Exception ex) {
            emailModel.setStatus(EmailStatus.ERROR);
        } finally {
            emailRepository.save(emailModel);
        }
    }
}
