package com.example.library_management.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReaderUpdateDTO {
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    private String phone;
    private String address;
    private MultipartFile avatarImage;
}
