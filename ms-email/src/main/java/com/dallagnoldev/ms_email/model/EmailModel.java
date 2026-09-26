package com.dallagnoldev.ms_email.model;

import com.dallagnoldev.ms_email.enums.EmailStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_emails")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID emailId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "email_from", nullable = false)
    private String emailFrom;

    @Column(name = "email_to", nullable = false)
    private String emailTo;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_status", nullable = false)
    private EmailStatus status;

    @Column(name = "send_date_email", nullable = false)
    private LocalDateTime sendDateEmail;
}
