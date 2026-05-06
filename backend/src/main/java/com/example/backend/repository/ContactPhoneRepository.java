package com.example.backend.repository;

import com.example.backend.entity.ContactPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactPhoneRepository extends JpaRepository<ContactPhone, Long> {

    List<ContactPhone> findAllByContactId(Long contactId);

    void deleteAllByContactId(Long contactId);
}