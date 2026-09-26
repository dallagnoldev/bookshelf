package com.dallagnoldev.ms_user.repository;

import com.dallagnoldev.ms_user.enums.UserRoles;
import com.dallagnoldev.ms_user.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRoleRepository extends JpaRepository<RoleModel, UUID> {
    Optional<RoleModel> findRoleByName(UserRoles name);
}
