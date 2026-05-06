package com.example.backend.dto.contact;

import com.example.backend.entity.ContactPhone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneDto {

    private Long id;                            // null on create, populated on update

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9\\s\\-]{7,15}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank(message = "Label is required")
    @Pattern(regexp = "^(mobile|work|home)$", message = "Label must be mobile, work, or home")
    private String label;

    private boolean isPrimary;

    // Outbound — build from entity
    public static PhoneDto from(ContactPhone phone) {
        PhoneDto dto = new PhoneDto();
        dto.setId(phone.getId());
        dto.setPhoneNumber(phone.getPhoneNumber());
        dto.setLabel(String.valueOf(phone.getLabel()));
        dto.setPrimary(phone.isPrimary());
        return dto;
    }
}