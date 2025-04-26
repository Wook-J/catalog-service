package com.polarbookshop.catalogservice.web;
// ex 3.10 REST 엔드포인트에 대한 핸들러 정의
// ex 3.13 요청 본문으로 전달된 책 데이터의 유효성 검사(@RequestBody 가 들어올 때 검사)

import javax.validation.Valid;			// gradle 2.7.3 버전 시
//import jakarta.validation.Valid;		// gradle 3.4.4 버전 시

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookService;


/*
 * @RestController : 클래스가 스프링 컴포넌트이고 REST 엔드 포인트를 위한 핸들러를 
 * 					 제공한다는 것을 표시하는 스테레오 타입 어노테이션
 * @RequestMapping : 클래스가 핸들러를 제공하는 루트 패스 URI("/books")를 인식
 * */
@RestController
@RequestMapping("books")
public class BookController {
	private final BookService bookService;
	
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}
	
	// HTTP GET 메서드를 특정 핸들러 메서드로 연결
	@GetMapping
	public Iterable<Book> get(){
		return bookService.viewBookList();
	}
	
	// 루트 패스 URI에 추가되는 URI 템플릿 변수("/books/{isbn}")
	@GetMapping("{isbn}")
	public Book getByIsbn(@PathVariable String isbn) {
		return bookService.viewBookDetails(isbn);
	}
	
	// HTTP POST 요청을 특정 핸들러 메서드로 연결
	// 책이 성공적으로 생성되면 201 상태 코드를 반환
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Book post(@Valid @RequestBody Book book) {
		return bookService.addBookToCatalog(book);
	}
	
	// HTTP DELETE 요청을 특정 핸들러 메서드로 연결
	// 책이 성공적으로 삭제되면 204 상태 코드를 반환
	@DeleteMapping("{isbn}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String isbn) {
		bookService.removeBookFromCatalog(isbn);
	}
	
	// HTTP PUT 요청을 특정 핸들러 메서드로 연결
	@PutMapping("{isbn}")
	public Book put(@PathVariable String isbn, @Valid @RequestBody Book book) {
		return bookService.editBookDetails(isbn, book);
	}
}
