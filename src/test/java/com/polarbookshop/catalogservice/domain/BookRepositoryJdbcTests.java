package com.polarbookshop.catalogservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.polarbookshop.catalogservice.config.DataConfig;

@DataJdbcTest	// 스프링 데이터 JDBC 컴포넌트를 집중적으로 테스트하는 클래스
@Import(DataConfig.class)	// 데이터 설정을 임포트(감사를 활성화하기 위해 필요)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)	// 내장 테스트 DB 사용 비활성화
@ActiveProfiles("integration")	// application-integration.yml에서 설정 로드하는 경우
class BookRepositoryJdbcTests {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired	// DB와 상호작용하기 위한 하위 수준의 객체
	private JdbcAggregateTemplate jdbcAggregateTemplate;
	
	@Test
	void findAllBooks() {
		var book1 = Book.of("1234561235", "Title", "Author", 12.90, "Polarsophia");
		var book2 = Book.of("1234561236", "Another Title", "Author", 12.90, "Polarsophia");
		jdbcAggregateTemplate.insert(book1);
		jdbcAggregateTemplate.insert(book2);

		Iterable<Book> actualBooks = bookRepository.findAll();

		assertThat(StreamSupport.stream(actualBooks.spliterator(), true)
			.filter(book -> book.isbn().equals(book1.isbn()) || book.isbn().equals(book2.isbn()))
			.collect(Collectors.toList())).hasSize(2);
    }
	
	@Test
	void findBookByIsbnWhenExisting() {
		var bookIsbn = "1234561237";
		var book = Book.of(bookIsbn, "Title", "Author", 12.90, "Polarsophia");
		jdbcAggregateTemplate.insert(book);
		Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);

		assertThat(actualBook).isPresent();
		assertThat(actualBook.get().isbn()).isEqualTo(book.isbn());

	}
	
	@Test
	void findBookByIsbnWhenNotExisting() {
		Optional<Book> actualBook = bookRepository.findByIsbn("1234561238");
		assertThat(actualBook).isEmpty();
    }
}
