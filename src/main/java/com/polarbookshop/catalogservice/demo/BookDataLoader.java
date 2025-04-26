package com.polarbookshop.catalogservice.demo;
// ex 4.6 testdata 프로파일이 활성일 때 도서 테스트 데이터 로드

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;

/* @Component : 스프링 빈을 정의하는 기본적인 어노테이션
 * - 이 클래스는 스프링 컨테이너에 의해 자동으로 빈으로 등록
 * -> 해당 클래스의 인스턴스가 스프링 애플리케이션 컨텍스트에 의해 관리됨
 * 
 * @Profile("testdata") : 이 클래스를 testdata 프로파일에 할당
 * - 이 클래스는 testdata 프로파일이 활성화될 때만 로드됨
 * - 애플리케이션이 실행될 때 'testdata' 프로파일이 활성화되면 해당 클래스를 빈으로 등록
 * - 주로 환경에 따라 다른 설정을 하고자할 때 사용
 * */
@Component
@Profile("testdata")
public class BookDataLoader {
	private final BookRepository bookRepository;
	
	public BookDataLoader(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	// ApplicationReadyEvent 발생 시 테스트 데이터 생성 시작
	@EventListener(ApplicationReadyEvent.class)
	public void loadBookTestData() {
		
		// 빈 DB로 시작하기 위해 기존 책이 있다면 모두 삭제
		bookRepository.deleteAll();
		
		// 기존 : 새로운 Book 객체 만듦
		// Book.java에서 @Id, @Version 처리 후 프레임 워크 내부적으로 식별자와 버전에 대한 할당 값을 처리
		
//		var book1 = new Book("1234567891", "Northern Lights", "Lyra Silverstar", 9.90);
//		var book2 = new Book("1234567892", "Polar Journey", "Iorek Polar Journey", 12.90);
		var book1 = Book.of("1234567891", "Northern Lights", "Lyra Silverstar", 9.90, "Polarsophia");
		var book2 = Book.of("1234567892", "Polar Journey", "Iorek Polar Journey", 12.90, "Polarsophia");
		
//		bookRepository.save(book1);
//		bookRepository.save(book2);
		
		// 여러 객체를 한번에 저장
		bookRepository.saveAll(List.of(book1, book2));
	}
}
