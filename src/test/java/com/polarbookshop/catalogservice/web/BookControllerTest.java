package com.polarbookshop.catalogservice.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;

@WebMvcTest(BookController.class)
@AutoConfigureRestTestClient
class BookControllerTest {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private BookService bookService;

    @Test
    void whenGetBooks_thenReturnsBookList() {
        Book book1 = new Book("1234567890", "Title One", "Author One", 9.99);
        Book book2 = new Book("1234567890123", "Title Two", "Author Two", 19.99);
        given(bookService.viewBookList()).willReturn(List.of(book1, book2));

        restTestClient.get()
                .uri("/books")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book[].class).value(books -> {
                    assertThat(books).hasSize(2);
                    assertThat(books[0].isbn()).isEqualTo("1234567890");
                    assertThat(books[1].isbn()).isEqualTo("1234567890123");
                });
    }

    @Test
    void whenGetBooks_thenReturnsEmptyList() {
        given(bookService.viewBookList()).willReturn(List.of());

        restTestClient.get()
                .uri("/books")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book[].class).value(books -> {
                    assertThat(books).isEmpty();
                });
    }

    @Test
    void whenGetBookByIsbn_thenReturnsBook() {
        String isbn = "1234567890";
        Book expected = new Book(isbn, "Title", "Author", 9.99);
        given(bookService.viewBookDetails(isbn)).willReturn(expected);

        restTestClient.get()
                .uri("/books/" + isbn)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class).value(book -> {
                    assertThat(book).isEqualTo(expected);
                });
    }

    @Test
    void whenGetBookByIsbnNotFound_thenReturns404() {
        String isbn = "1234567890";
        given(bookService.viewBookDetails(isbn)).willThrow(new BookNotFoundException(isbn));

        restTestClient.get()
                .uri("/books/" + isbn)
                .exchange()
                .expectStatus().isNotFound();
    }
}
