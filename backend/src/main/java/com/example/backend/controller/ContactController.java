package com.example.backend.controller;

import com.example.backend.config.CurrentUser;
import com.example.backend.dto.contact.ContactRequest;
import com.example.backend.dto.contact.ContactResponse;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<List<ContactResponse>> getAll(
            @CurrentUser Long userId,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(contactService.getAll(userId, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getById(
            @PathVariable Long id,
            @CurrentUser Long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(contactService.getById(id, userId));
    }

    @PostMapping
    public ResponseEntity<ContactResponse> create(
            @Valid @RequestBody ContactRequest req,
            @CurrentUser Long userId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactService.create(req, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ContactRequest req,
            @CurrentUser Long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(contactService.update(id, req, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @CurrentUser Long userId) throws ResourceNotFoundException {
        contactService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}