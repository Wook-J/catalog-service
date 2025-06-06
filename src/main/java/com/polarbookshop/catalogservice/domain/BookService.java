package com.polarbookshop.catalogservice.domain;
// ex 3.5 애플리케이션 사용 사례 구현

import org.springframework.stereotype.Service;

@Service	// 스프링이 관리하는 서비스라는 것을 표시하는 스테레오 타입 어노테이션
public class BookService {
	private final BookRepository bookRepository;
	
	// BookRepository 는 생성자 AutoWiring을 통해 제공
	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	public Iterable<Book> viewBookList(){
		return bookRepository.findAll();
	}
	
	public Book viewBookDetails(String isbn) {
		// 존재하지 않는 책을 보려할 때 예외 발생
		return bookRepository.findByIsbn(isbn)
			.orElseThrow(() -> new BookNotFoundException(isbn));
	}
	
	public Book addBookToCatalog(Book book) {
		// 동일한 책을 여러번 추가하려고 시도 시 그에 해당하는 예외 발생
		if(bookRepository.existsByIsbn(book.isbn())) {
			throw new BookAlreadyExistsException(book.isbn());
		}
		return bookRepository.save(book);
	}
	
	public void removeBookFromCatalog(String isbn) {
		bookRepository.deleteByIsbn(isbn);
	}
	
	public Book editBookDetails(String isbn, Book book) {
		return bookRepository.findByIsbn(isbn)
			.map(existingBook -> {
				var bookToUpdate = new Book(
					existingBook.id(),			// 기존 책의 식별자를 사용
					existingBook.isbn(),
					book.title(),
					book.author(),
					book.price(),
					book.publisher(),
					existingBook.createdDate(),	// 기존 책 레코드의 생성날짜
					existingBook.lastModifiedDate(),	// 기존 책 레코드의 마지막 수정날짜, 업데이트 성공 시 스프링 데이터에 의해 자동으로 변경
					existingBook.version());	// 기존 책 버전 사용 시 업데이트가 성공하면 자동으로 증가
				return bookRepository.save(bookToUpdate);
			})
			.orElseGet(() -> addBookToCatalog(book));
	}
}