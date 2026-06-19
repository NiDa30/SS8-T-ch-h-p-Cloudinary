package com.example.library_management.service;

import com.example.library_management.dto.BorrowCreateDTO;
import com.example.library_management.entity.Borrow;
import com.example.library_management.entity.BorrowStatus;
import com.example.library_management.exception.BookAlreadyReturnedException;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.BorrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookService bookService;

    @Autowired
    public BorrowService(BorrowRepository borrowRepository, BookService bookService) {
        this.borrowRepository = borrowRepository;
        this.bookService = bookService;
    }

    public Borrow createBorrow(BorrowCreateDTO dto) {
        Borrow borrow = new Borrow();
        borrow.setUsername(dto.getUsername());
        borrow.setBookId(dto.getBookId());
        return borrowRepository.save(borrow);
    }

    public Borrow returnBook(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow ticket not found with id: " + id));

        if (borrow.getStatus() == BorrowStatus.RETURNED) {
            throw new BookAlreadyReturnedException("Sách này đã được trả rồi");
        }

        borrow.setStatus(BorrowStatus.RETURNED);
        borrow.setReturnDate(LocalDate.now());

        bookService.incrementStock(borrow.getBookId());

        return borrowRepository.save(borrow);
    }
}
