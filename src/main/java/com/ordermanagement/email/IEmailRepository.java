package com.ordermanagement.email;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IEmailRepository extends JpaRepository<Email, UUID> {
}
