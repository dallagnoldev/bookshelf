package com.dallagnoldev.ms_email.repository;

import com.dallagnoldev.ms_email.model.EmailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IEmailRepository extends JpaRepository<EmailModel, UUID> {
}
