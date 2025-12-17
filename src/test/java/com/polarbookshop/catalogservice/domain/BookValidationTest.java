package com.polarbookshop.catalogservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class BookValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void givenIsbnHas10Digits_whenAllFieldsValid_thenNoViolations() {
        Book book = new Book("1234567890", "Title", "Author", 9.99);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void givenIsbnHas13Digits_whenAllFieldsValid_thenNoViolations() {
        Book book = new Book("1234567890123", "Title", "Author", 9.99);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnIsBlank_thenViolation() {
        Book book = new Book("", "Title", "Author", 9.99);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isNotEmpty();
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("must not be blank");
    }

    @Test
    void whenIsbnIsNull_thenViolation() {
        Book book = new Book(null, "Title", "Author", 9.99);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
    }

    @Test
    void whenIsbnHasInvalidFormat_thenViolation() {
        Book book = new Book("12345", "Title", "Author", 9.99);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must match \"^([0-9]{10}|[0-9]{13})$\"");
    }

    @Test
    void whenIsbnContainsNonDigits_thenViolation() {
        Book book = new Book("123456789X", "Title", "Author", 9.99);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must match \"^([0-9]{10}|[0-9]{13})$\"");
    }

    @Test
    void whenTitleIsBlank_thenViolation() {
        Book book = new Book("1234567890", "", "Author", 9.99);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
    }

    @Test
    void whenTitleIsNull_thenViolation() {
        Book book = new Book("1234567890", null, "Author", 9.99);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
    }

    @Test
    void whenAuthorIsBlank_thenViolation() {
        Book book = new Book("1234567890", "Title", "", 9.99);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
    }

    @Test
    void whenAuthorIsNull_thenViolation() {
        Book book = new Book("1234567890", "Title", null, 9.99);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be blank");
    }

    @Test
    void whenPriceIsNull_thenViolation() {
        Book book = new Book("1234567890", "Title", "Author", null);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must not be null");
    }

    @Test
    void whenPriceIsZero_thenViolation() {
        Book book = new Book("1234567890", "Title", "Author", 0.0);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must be greater than 0");
    }

    @Test
    void whenPriceIsNegative_thenViolation() {
        Book book = new Book("1234567890", "Title", "Author", -9.99);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting(ConstraintViolation::getMessage).contains("must be greater than 0");
    }

     @Test
  void whenMultipleFieldsInvalid_thenMultipleViolations() {
      Book book = new Book("", "", "", null);
      Set<ConstraintViolation<Book>> violations = validator.validate(book);
      assertThat(violations).isNotEmpty();
  }
}
