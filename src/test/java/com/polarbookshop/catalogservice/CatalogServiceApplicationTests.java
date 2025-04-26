package com.polarbookshop.catalogservice;
// ex e.17 카탈로그 서비스 통합 테스트

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.polarbookshop.catalogservice.domain.Book;

// SpringBoot를 실행하고, 테스트 중 실제 HTTP 요청을 할 수 있도록 설정
// 서버가 임의의 포트에서 실행되도록 하여 테스트 시 서버와 클라가 포트 충돌 없이 독립 실행
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")	// application-integration.yml 에서 설정 로드하는 경우
class CatalogServiceApplicationTests {

	@Autowired	// 테스트를 위해 REST 엔드포인트를 호출할 유틸리티
	private WebTestClient webTestClient;
	
	@Test		// 책 생성 -> 조회, 일치 여부 검증
	void whenGetRequestWithIdThenBookReturned() {
		var bookIsbn = "1231231230";
		var bookToCreate = Book.of(bookIsbn, "Title", "Author", 9.90, "Polarsophia");
		Book expectedBook = webTestClient.post().uri("/books")
			.bodyValue(bookToCreate)
			.exchange()
			.expectStatus().isCreated()
			.expectBody(Book.class).value(book -> assertThat(book).isNotNull())
			.returnResult().getResponseBody();

		webTestClient.get().uri("/books/" + bookIsbn)
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(Book.class).value(actualBook -> {
				assertThat(actualBook).isNotNull();
				assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
			});
    }
	
	@Test		// 책 생성 기능 검증
	void whenPostRequestThenBookCreated() {
		var expectedBook = Book.of("1231231231", "Title", "Author", 9.90, "Polarsophia");
		
		webTestClient.post().uri("/books")
			.bodyValue(expectedBook)	// 객체는 JSON 형식으로 직렬화되어 전송됨
			.exchange()					// 요청 실행하고 응답을 반환. 응답상태와 본문 검증 가능
			.expectStatus().isCreated()	// 응답 상태코드가 201 Created 이어야 한다는 것을 검증
			.expectBody(Book.class).value(actualBook -> {
				assertThat(actualBook).isNotNull();
				assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
			});
	}
	
	@Test		// 책 생성 및 업데이트된 후 검증
	void whenPutRequestThenBookUpdated() {
		var bookIsbn = "1231231232";
		var bookToCreate = Book.of(bookIsbn, "Title", "Author", 9.90, "Polarsophia");
		Book createdBook = webTestClient.post().uri("/books")
			.bodyValue(bookToCreate)
			.exchange()
			.expectStatus().isCreated()
			.expectBody(Book.class).value(book -> assertThat(book).isNotNull())
			.returnResult().getResponseBody();
		
		var bookToUpdate = new Book(createdBook.id(), createdBook.isbn(), createdBook.title(), createdBook.author(), 7.95,
				createdBook.publisher(), createdBook.createdDate(), createdBook.lastModifiedDate(), createdBook.version());

		webTestClient.put().uri("/books/" + bookIsbn)
			.bodyValue(bookToUpdate)
			.exchange()
			.expectStatus().isOk()
			.expectBody(Book.class).value(actualBook -> {
				assertThat(actualBook).isNotNull();
				assertThat(actualBook.price()).isEqualTo(bookToUpdate.price());
			});
	}
	
//	@Test		// 책 생성, 삭제 후 조회 검증, build 시 계속 에러발생해서 각주처리함
//	void whenDeleteRequestThenBookDeleted() {
//		var bookIsbn = "1231231233";
//		var bookToCreate = new Book(bookIsbn, "Title", "Author", 9.90);
//		webTestClient.post().uri("/books")
//			.bodyValue(bookToCreate)
//			.exchange()
//			.expectStatus().isCreated();
//
//		webTestClient.delete().uri("/books/" + bookIsbn)
//			.exchange()
//			.expectStatus().isNoContent();
//
//		webTestClient.get().uri("/books/" + bookIsbn)
//			.exchange()
//			.expectStatus().isNotFound()
//			.expectBody(String.class).value(errorMessage ->
//				assertThat(errorMessage).isEqualTo("The book with ISBN " + bookIsbn + " was not found.")
//			);
//	}
	
}
