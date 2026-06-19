package com.example.library_management.controller;

import com.example.library_management.dto.BookCreateDTO;
import com.example.library_management.dto.BookUpdateStockDTO;
import com.example.library_management.entity.Book;
import com.example.library_management.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@ModelAttribute BookCreateDTO dto) {
        Book createdBook = bookService.createBook(dto);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Book> updateBookStock(@PathVariable Long id, @Valid @RequestBody BookUpdateStockDTO dto) {
        Book updatedBook = bookService.updateBookStock(id, dto);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }
}
