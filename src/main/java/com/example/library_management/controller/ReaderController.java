package com.example.library_management.controller;

import com.example.library_management.dto.ReaderCreateDTO;
import com.example.library_management.dto.ReaderUpdateDTO;
import com.example.library_management.entity.Reader;
import com.example.library_management.service.ReaderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @PostMapping
    public ResponseEntity<Reader> createReader(@ModelAttribute @Valid ReaderCreateDTO dto) {
        Reader createdReader = readerService.createReader(dto);
        return new ResponseEntity<>(createdReader, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable Long id) {
        Reader reader = readerService.getReaderById(id);
        return ResponseEntity.ok(reader);
    }

    @GetMapping
    public ResponseEntity<List<Reader>> getAllReaders() {
        List<Reader> readers = readerService.getAllReaders();
        return ResponseEntity.ok(readers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reader> updateReader(@PathVariable Long id, @ModelAttribute @Valid ReaderUpdateDTO dto) {
        Reader updatedReader = readerService.updateReader(id, dto);
        return ResponseEntity.ok(updatedReader);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) {
        readerService.deleteReader(id);
        return ResponseEntity.noContent().build();
    }
}
