package com.example.library_management;

import com.example.library_management.entity.Book;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.repository.BorrowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BorrowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    @BeforeEach
    void setUp() {
        borrowRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    public void testCreateBorrowSuccess() throws Exception {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setStock(5);
        book = bookRepository.save(book);

        String json = String.format("{\"username\": \"john_doe\", \"bookId\": %d}", book.getId());

        mockMvc.perform(post("/api/borrows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.bookId").value(book.getId()));
    }

    @Test
    public void testCreateBorrowInvalidBookId() throws Exception {
        String json = "{\"username\": \"john_doe\", \"bookId\": 999}";

        mockMvc.perform(post("/api/borrows")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Sách không tồn tại trong hệ thống")));
    }
}
