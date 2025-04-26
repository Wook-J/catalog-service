package com.polarbookshop.catalogservice.domain;
// ex 3.6 데이터 엑세스를 위해 도메인 계층이 사용하는 추상화

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

// CrudRepository : 엔티티(Book)와 기본 키 유형(Long)을 지정하면서 CRUD 연산을 제공하는 레파지토리
public interface BookRepository extends CrudRepository<Book, Long>{
//	Iterable<Book> findAll();					// CrudRepository 에서 기본 제공
//	Book save(Book book);						// CrudRepository 에서 기본 제공
	
	Optional<Book> findByIsbn(String isbn);		// 실행 시간에 스프링 데이터에 의해 구현이 제공되는 메서드
	boolean existsByIsbn(String isbn);
	
	@Modifying										// DB 상태를 수정할 연산
	@Transactional									// 메서드가 트랜잭션으로 실행됨
	@Query("delete from Book where isbn = :isbn")	// 스프링 데이터가 메서드 구현에 사용할 쿼리 선언
	void deleteByIsbn(String isbn);
	
	// Iterable : 반복자
	// Optional : 기존 메서드가 결과를 못찾으면 null을 반환할때 생기는 NPE 방지위해 명시적으로 표현
}
