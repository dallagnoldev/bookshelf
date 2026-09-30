package com.dallagnoldev.ms_books.producer;

import com.dallagnoldev.ms_books.dto.EmailDto;
import com.dallagnoldev.ms_books.dto.LoanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoanProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.loan.name}")
    private String routingKey;

    public void receiptEmail(LoanResponse response) {
        String body =
                "\nTitle: " + response.book().title() +
                "\nLoan Date: " + response.loanDate() +
                "\nReturn Loan Date: " + response.returnLoanDate();

        EmailDto emailDto = EmailDto.builder()
                .emailTo(response.userEmail())
                .subject("Bookshelf Receipt - Loan #" + response.loanId())
                .text(body)
                .build();

        rabbitTemplate.convertAndSend("", routingKey, emailDto);

    }
}
