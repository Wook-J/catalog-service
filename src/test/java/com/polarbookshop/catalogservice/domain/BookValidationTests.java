package com.polarbookshop.catalogservice.domain;
// ex 3.15 Book 객체의 유효성 검사 제약 조건을 검증하기 위한 단위 테스트

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;			// gradle 2.7.3 버전 시
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
//import jakarta.validation.ConstraintViolation;		// gradle 3.4.4 버전 시
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BookValidationTests {
	// 객체를 검사하는 엔진(@NotBlank, @Pattern 등)
	private static Validator validator;
	
	// 모든 테스트 메서드 실행 전 딱 1번 호출
	@BeforeAll
	static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@Test
	void whenAllFieldsCorrectThenValidationSucceeds() {
		var book = Book.of("1234567890", "Title", "Author", 9.90, "Polarsophia");
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).isEmpty();
	}
	
	@Test
	void whenIsbnNotDefinedThenValidationFails() {
		var book = Book.of("", "Title", "Author", 9.90, "Polarsophia");
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(2);
		List<String> constraintViolationMessages = violations.stream()
			.map(ConstraintViolation::getMessage).collect(Collectors.toList());
		assertThat(constraintViolationMessages)
			.contains("The book ISBN must be defined.")
			.contains("The ISBN format must be valid.");
	}
	
	@Test
	void whenIsbnDefinedButIncorrectThenValidationFails() {
		var book = Book.of("a234567890", "Title", "Author", 9.90, "Polarsophia");
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);		// ISBN 필드 하나만 에러가 있어야 한다는 의미
		assertThat(violations.iterator().next().getMessage())
			.isEqualTo("The ISBN format must be valid.");
	}
	
	@Test
	void whenTitleIsNotDefinedThenValidationFails() {
		var book = Book.of("1234567890", "", "Author", 9.90, "Polarsophia");
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator().next().getMessage())
			.isEqualTo("The book title must be defined.");
	}
	
	@Test
	void whenAuthorIsNotDefinedThenValidationFails() {
		var book = Book.of("1234567890", "Title", "", 9.90, "Polarsophia");
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator().next().getMessage())
			.isEqualTo("The book author must be defined.");
	}
	
	@Test
	void whenPriceIsNotDefinedThenValidationFails() {
		var book = Book.of("1234567890", "Title", "Author", null, "Polarsophia");
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator().next().getMessage())
			.isEqualTo("The book price must be defined.");
	}
	
	@Test
	void whenPriceDefinedButZeroThenValidationFails() {
		var book = Book.of("1234567890", "Title", "Author", 0.0, "Polarsophia");
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator().next().getMessage())
			.isEqualTo("The book price must be greater than zero.");
	}
	
	@Test
	void whenPriceDefinedButNegativeThenValidationFails() {
		var book = Book.of("1234567890", "Title", "Author", -9.90, "Polarsophia");
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator().next().getMessage())
			.isEqualTo("The book price must be greater than zero.");
	}

	@Test
	void whenPublisherIsNotDefinedThenValidationSucceeds() {
		Book book = Book.of("1234567890", "Title", "Author", 9.90, null);
		Set<ConstraintViolation<Book>> violations = validator.validate(book);
		assertThat(violations).isEmpty();
	}
}
