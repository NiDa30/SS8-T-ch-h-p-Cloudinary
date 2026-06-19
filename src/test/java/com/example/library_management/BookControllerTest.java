package com.example.library_management;

import com.example.library_management.entity.Book;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.service.CloudinaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @MockBean
    private CloudinaryService cloudinaryService;

    @Test
    public void testCreateBookWithImage() throws Exception {
        when(cloudinaryService.uploadImage(any())).thenReturn("https://res.cloudinary.com/demo/image/upload/test-cover.jpg");

        MockMultipartFile file = new MockMultipartFile(
                "coverImage",
                "test-cover.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake image content".getBytes()
        );

        mockMvc.perform(multipart("/api/books")
                .file(file)
                .param("title", "Spring Boot in Action")
                .param("author", "Craig Walls")
                .param("stock", "10"))
                .andExpect(status().isCreated());

        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(1);
        Book book = books.get(0);
        assertThat(book.getTitle()).isEqualTo("Spring Boot in Action");
        assertThat(book.getCoverUrl()).isEqualTo("https://res.cloudinary.com/demo/image/upload/test-cover.jpg");
    }
}
