package com.polarbookshop.catalogservice.domain;
// ex 3.6 데이터 엑세스를 위해 도메인 계층이 사용하는 추상화

import java.util.Optional;

public interface BookRepository {
	Iterable<Book> findAll();
	Optional<Book> findByIsbn(String isbn);
	boolean existsByIsbn(String isbn);
	Book save(Book book);
	void deleteByIsbn(String isbn);
	
	// Iterable : 반복자
	// Optional : 기존 메서드가 결과를 못찾으면 null을 반환할때 생기는 NPE 방지위해 명시적으로 표현
}
