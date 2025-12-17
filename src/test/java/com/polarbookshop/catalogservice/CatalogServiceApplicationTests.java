package com.polarbookshop.catalogservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.polarbookshop.catalogservice.domain.Book;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class CatalogServiceApplicationTests {

	@Autowired
	private RestTestClient restTestClient;

	@Test
	void whenPostRequest_thenBookCreated() {
		Book input = new Book("0123456789", "Title", "Author", 9.90);
		Book expected = new Book("0123456789", "Title", "Author", 9.90);

		restTestClient.post()
				.uri("/books")
				.body(input)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actual -> {
					assertThat(actual).isEqualTo(expected);
				});
	}

}
