package com.polarbookshop.catalogservice.persistence;
// ex 3.7 BookRepository 인터페이스의 인메모리 구현

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;

@Repository		// 클래스가 스프링에 의해 관리되는 레파지토리임을 표시하는 스테레오 타입
public class InMemoryBookRepository implements BookRepository{
	// 테스트 목적으로 책을 저장하기 위한 인메모리 맵
	// ConcurrentHashMap : 멀티스레드 환경에서 안전하게 쓸 수 있는 HashMap
	// 여러 스레드가 동시에 접근해도 문제없이 작동하게 lock 을 세밀하게 걸어둠
	// key, value 모두 null 을 허용하지 않음
	private static final Map<String, Book> books = new ConcurrentHashMap<>();
	
	@Override
	public Iterable<Book> findAll() {
		return books.values();
	}
	
	@Override
	public Optional<Book> findByIsbn(String isbn) {
		return existsByIsbn(isbn) ? Optional.of(books.get(isbn)) : Optional.empty();
	}
	
	@Override
	public boolean existsByIsbn(String isbn) {
		// isbn 을 얻었는 데 null 이 아니면 true 반환
		return books.get(isbn) != null;
	}
	
	@Override
	public Book save(Book book) {
		books.put(book.isbn(), book);
		return book;
	}
	
	@Override
	public void deleteByIsbn(String isbn) {
		books.remove(isbn);
	}
}
