package com.polarbookshop.catalogservice.web;

import static org.mockito.BDDMockito.given;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;

@WebMvcTest(BookController.class)
public class BookControllerMvcTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private BookService bookService;

    @Test
    void givenBookDoesNotExistWhenGetBookThenReturn404() throws Exception {
        String isbn = "1234567890";
        given(bookService.viewBookDetails(isbn)).willThrow(BookNotFoundException.class);

        MvcTestResult result = mockMvcTester.get().uri("/books/{isbn}", isbn).accept(MediaType.APPLICATION_JSON)
                .exchange();

        Assertions.assertThat(result).hasStatus(HttpStatus.NOT_FOUND);
    }

}
