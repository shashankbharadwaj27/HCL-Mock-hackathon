package com.example.backend.repository;

import com.example.backend.entity.ContactPhone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactPhoneRepository extends JpaRepository<ContactPhone, Long> {
}