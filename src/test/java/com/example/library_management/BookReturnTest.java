package com.example.library_management;

import com.example.library_management.entity.Book;
import com.example.library_management.entity.Borrow;
import com.example.library_management.entity.BorrowStatus;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.repository.BorrowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookReturnTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    private Long bookId;
    private Long borrowId;

    @BeforeEach
    void setUp() {
        borrowRepository.deleteAll();
        bookRepository.deleteAll();

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setStock(10);
        book = bookRepository.save(book);
        bookId = book.getId();

        Borrow borrow = new Borrow();
        borrow.setUsername("user1");
        borrow.setBookId(bookId);
        borrow.setStatus(BorrowStatus.BORROWING);
        borrow = borrowRepository.save(borrow);
        borrowId = borrow.getId();
    }

    @Test
    public void testReturnBookSuccess() throws Exception {
        mockMvc.perform(patch("/api/borrows/" + borrowId + "/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RETURNED"))
                .andExpect(jsonPath("$.returnDate").exists());

        Book updatedBook = bookRepository.findById(bookId).orElseThrow();
        assertThat(updatedBook.getStock()).isEqualTo(11);
    }

    @Test
    public void testReturnBookAlreadyReturned() throws Exception {
        // Return once
        mockMvc.perform(patch("/api/borrows/" + borrowId + "/return"))
                .andExpect(status().isOk());

        // Return again
        mockMvc.perform(patch("/api/borrows/" + borrowId + "/return"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Sách này đã được trả rồi"));
    }

    @Test
    public void testReturnBookNotFound() throws Exception {
        mockMvc.perform(patch("/api/borrows/999/return"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Borrow ticket not found with id: 999"));
    }
}
