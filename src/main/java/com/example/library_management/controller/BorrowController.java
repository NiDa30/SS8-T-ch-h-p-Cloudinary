package com.example.library_management.controller;

import com.example.library_management.dto.BorrowCreateDTO;
import com.example.library_management.entity.Borrow;
import com.example.library_management.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping
    public ResponseEntity<Borrow> createBorrow(@Valid @RequestBody BorrowCreateDTO dto) {
        Borrow borrow = borrowService.createBorrow(dto);
        return new ResponseEntity<>(borrow, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<Borrow> returnBook(@PathVariable Long id) {
        Borrow borrow = borrowService.returnBook(id);
        return ResponseEntity.ok(borrow);
    }
}
