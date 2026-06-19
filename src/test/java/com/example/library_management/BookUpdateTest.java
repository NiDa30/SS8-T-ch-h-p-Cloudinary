package com.example.library_management;

import com.example.library_management.entity.Book;
import com.example.library_management.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    private Long existingBookId;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setStock(10);
        book = bookRepository.save(book);
        existingBookId = book.getId();
    }

    @Test
    public void testUpdateStockSuccess() throws Exception {
        String json = "{\"stock\": 20}";

        mockMvc.perform(patch("/api/books/update/" + existingBookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        Book updatedBook = bookRepository.findById(existingBookId).orElseThrow();
        assertThat(updatedBook.getStock()).isEqualTo(20);
    }

    @Test
    public void testUpdateStockNegative() throws Exception {
        String json = "{\"stock\": -5}";

        mockMvc.perform(patch("/api/books/update/" + existingBookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateStockNotFound() throws Exception {
        String json = "{\"stock\": 20}";

        mockMvc.perform(patch("/api/books/update/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }
}
