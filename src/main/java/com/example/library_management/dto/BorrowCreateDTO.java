package com.example.library_management.dto;

import com.example.library_management.validation.ExistingBookId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BorrowCreateDTO {
    @NotBlank(message = "Username is required")
    private String username;

    @NotNull(message = "Book ID is required")
    @ExistingBookId
    private Long bookId;
}
