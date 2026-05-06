package com.example.backend.dto.contact;

import com.example.backend.entity.Contact;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ContactResponse {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String notes;
    private final List<PhoneDto> phones;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ContactResponse(Contact contact) {
        this.id = contact.getId();
        this.firstName = contact.getFirstName();
        this.lastName = contact.getLastName();
        this.email = contact.getEmail();
        this.notes = contact.getNotes();
        this.phones = contact.getPhones()
                .stream()
                .map(PhoneDto::from)
                .toList();
        this.createdAt = contact.getCreatedAt();
        this.updatedAt = contact.getUpdatedAt();
    }
}