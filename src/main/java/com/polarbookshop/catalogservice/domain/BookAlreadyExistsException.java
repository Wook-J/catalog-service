package com.polarbookshop.catalogservice.domain;
// ex 3.8 이미 조재하는 책을 추가 시 발생시키는 예외

public class BookAlreadyExistsException extends RuntimeException{
	public BookAlreadyExistsException(String isbn) {
		super("A book with ISBN " + isbn + " already exists.");
	}
}
