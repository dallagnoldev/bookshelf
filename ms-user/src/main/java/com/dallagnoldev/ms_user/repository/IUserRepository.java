package com.dallagnoldev.ms_user.repository;

import com.dallagnoldev.ms_user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {
}
