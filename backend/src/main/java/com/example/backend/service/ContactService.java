package com.example.backend.service;

import com.example.backend.dto.contact.ContactRequest;
import com.example.backend.dto.contact.ContactResponse;
import com.example.backend.entity.Contact;
import com.example.backend.entity.ContactPhone;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserService userService;

    public List<ContactResponse> getAll(Long userId, String search) {
        List<Contact> contacts = (search != null && !search.isBlank())
                ? contactRepository.searchByUserId(userId, search)
                : contactRepository.findAllByUserId(userId);

        return contacts.stream()
                .map(ContactResponse::new)
                .toList();
    }

    public ContactResponse getById(Long contactId, Long userId) throws ResourceNotFoundException {
        Contact contact = findOwnedContact(contactId, userId);
        return new ContactResponse(contact);
    }

    @Transactional
    public ContactResponse create(ContactRequest req, Long userId) throws ResourceNotFoundException {
        User user = userService.findById(userId);
        validateSinglePrimary(req);

        Contact contact = Contact.builder()
                .user(user)
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .notes(req.getNotes())
                .build();

        req.getPhones().forEach(phoneDto -> {
            ContactPhone phone = ContactPhone.builder()
                    .contact(contact)
                    .phoneNumber(phoneDto.getPhoneNumber())
                    .label(ContactPhone.PhoneLabel.valueOf(phoneDto.getLabel()))
                    .isPrimary(phoneDto.isPrimary())
                    .build();
            contact.getPhones().add(phone);
        });

        contactRepository.save(contact);
        return new ContactResponse(contact);
    }

    @Transactional
    public ContactResponse update(Long contactId, ContactRequest req, Long userId) throws ResourceNotFoundException {
        Contact contact = findOwnedContact(contactId, userId);
        validateSinglePrimary(req);

        contact.setFirstName(req.getFirstName());
        contact.setLastName(req.getLastName());
        contact.setEmail(req.getEmail());
        contact.setNotes(req.getNotes());

        // replace phones — orphanRemoval handles deletes
        contact.getPhones().clear();
        req.getPhones().forEach(phoneDto -> {
            ContactPhone phone = ContactPhone.builder()
                    .contact(contact)
                    .phoneNumber(phoneDto.getPhoneNumber())
                    .label(ContactPhone.PhoneLabel.valueOf(phoneDto.getLabel()))
                    .isPrimary(phoneDto.isPrimary())
                    .build();
            contact.getPhones().add(phone);
        });

        contactRepository.save(contact);
        return new ContactResponse(contact);
    }

    @Transactional
    public void delete(Long contactId, Long userId) throws ResourceNotFoundException {
        Contact contact = findOwnedContact(contactId, userId);
        contactRepository.delete(contact);
    }

    // --- private helpers ---

    private Contact findOwnedContact(Long contactId, Long userId) throws ResourceNotFoundException {
        return contactRepository.findByIdAndUserId(contactId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
    }

    private void validateSinglePrimary(ContactRequest req) {
        long primaryCount = req.getPhones().stream()
                .filter(p -> p.isPrimary())
                .count();
        if (primaryCount != 1)
            throw new IllegalArgumentException("Exactly one phone must be marked as primary");
    }
}

