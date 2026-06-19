package com.example.library_management.service;

import com.example.library_management.dto.BookCreateDTO;
import com.example.library_management.dto.BookUpdateStockDTO;
import com.example.library_management.entity.Book;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public BookService(BookRepository bookRepository, CloudinaryService cloudinaryService) {
        this.bookRepository = bookRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public Book createBook(BookCreateDTO dto) {
        String coverUrl = null;
        MultipartFile file = dto.getCoverImage();

        if (file != null && !file.isEmpty()) {
            coverUrl = cloudinaryService.uploadImage(file);
        }

        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setStock(dto.getStock());
        book.setCoverUrl(coverUrl);

        return bookRepository.save(book);
    }

    public Book updateBookStock(Long id, BookUpdateStockDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        book.setStock(dto.getStock());
        return bookRepository.save(book);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    public void incrementStock(Long id) {
        Book book = getBookById(id);
        book.setStock(book.getStock() + 1);
        bookRepository.save(book);
    }
}
