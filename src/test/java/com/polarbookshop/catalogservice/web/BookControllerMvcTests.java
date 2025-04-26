package com.polarbookshop.catalogservice.web;
// ex 3.18 웹 MVC 슬라이스에 대한 통합테스트

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

// Spring MVC 컴포넌트에 중점을 두고, 명시적으로 BookController 클래스를 타깃으로 하는 테스트 클래스
@WebMvcTest(BookController.class)
class BookControllerMvcTests {
	
	@Autowired	// 모의 환경에서 웹 계층을 테스트하기 위한 유틸리티 클래스
	private MockMvc mockMvc;
	
	@MockBean	// 스프링 애플리케이션 콘텍스트에 BookService의 모의 객체 추가
	private BookService bookService;
	
	@Test
	void whenGetBookNotExistingThenShouldReturn404() throws Exception{
		String isbn = "73737313940";
		given(bookService.viewBookDetails(isbn))
			.willThrow(BookNotFoundException.class);	// 모의 빈이 어떻게 작동할 것인지 규정
		mockMvc.perform(get("/books/" + isbn))			// MockMvc는 HTTP Get 요청 수행 후 결과 확인 위해 사용
			.andExpect(status().isNotFound());			// 응답이 "404 발견된지 않음" 상태를 가질 것으로 예상
	}
}
