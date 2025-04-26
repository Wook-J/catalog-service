package com.polarbookshop.catalogservice.domain;
// ex 3.9 책을 찾을 수 없을 때 발생하는 예외

public class BookNotFoundException extends RuntimeException{
	public BookNotFoundException(String isbn) {
		super("The Book with ISBN " + isbn + " was not found.");
	}
}
