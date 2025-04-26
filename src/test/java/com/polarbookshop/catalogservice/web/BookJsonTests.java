package com.polarbookshop.catalogservice.web;
// ex 3.19 JSON 슬라이스에 대한 통합테스트

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import com.polarbookshop.catalogservice.domain.Book;

@JsonTest	// JSON 관련 테스트만 수행하는 가벼운 슬라이스 테스트(웹서버 없이 JSON 관련 Bean 테스트)
class BookJsonTests {
	
	@Autowired	// JSON 직렬화 및 역직렬화를 확인하기 위한 유틸리티 클래스
	private JacksonTester<Book> json;
	
	@Test
	void testSerialize() throws Exception{
		var book = new Book("1234567890", "Title", "Author", 9.90);
		var jsonContent = json.write(book);		// JSON 문자열로 변환
		
		// 변환된 JSON에서 isbn, title, author, price 필드값을 꺼내서 변환이 정확한지 테스트
		assertThat(jsonContent).extractingJsonPathStringValue("@.isbn").isEqualTo(book.isbn());
		assertThat(jsonContent).extractingJsonPathStringValue("@.title").isEqualTo(book.title());
		assertThat(jsonContent).extractingJsonPathStringValue("@.author").isEqualTo(book.author());
		assertThat(jsonContent).extractingJsonPathNumberValue("@.price").isEqualTo(book.price());
	}
	
	@Test
	void testDeserialize() throws Exception {
		// 자바 텍스트 블록 기능을 사용해 JSON 객체를 정의(문자열로 JSON을 직접 만듦)
		var content = """
			{
				"isbn": "1234567890",
				"title": "Title",
				"author": "Author",
				"price": 9.90 
			}
			""";
		
		// JSON에서 자바 객체로의 변환을 확인
		assertThat(json.parse(content))
			.usingRecursiveComparison()
			.isEqualTo(new Book("1234567890", "Title", "Author", 9.90));
		
		/* usingRecursiveComparison : 객체를 필드 하나하나 다 비교하는 방법
		 * - 깊게 들여다봐서 객체 안에 또 객체가 있으면 그것까지 전부 재귀하게 비교해줌
		 * - equals() 없이, 필드값으로 비교
		 * - 객체가 복잡하거나 중첩되도 알아서 파고 들어가서 비교함
		 * - JSON 변환 테스트, DTO 테스트, 복잡한 도메인 테스트할 때 매우 유용
		 * */
	}
}
