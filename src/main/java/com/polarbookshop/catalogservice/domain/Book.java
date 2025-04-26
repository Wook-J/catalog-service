package com.polarbookshop.catalogservice.domain;
// ex 3.4 도메인 개체 정의
// ex 3.12 각 필드에 대해 정의한 유효성 제약

import javax.validation.constraints.NotBlank;		// gradle 2.7.3 버전 시
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

//import jakarta.validation.constraints.NotBlank;	// gradle 3.4.4 버전 시
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Positive;

/*  Record 타입 : 불변 데이터 객체를 간단하게 선언하는 문법
 * () 내부 : 생성자 파라미터 정의 -> 자동으로 필드, 생성자, getter, toString(), equals(), hashCode() 같은 메서드 생성
 * 이유 : "필드 = 생성자 파라미터"가 1:1로 대응됨
 * */
public record Book(
		
	@NotBlank(message="The book ISBN must be defined.")
	@Pattern(regexp="^([0-9]{10}|[0-9]{13})$", message="The ISBN format must be valid.")
	String isbn,
	
	@NotBlank(message="The book title must be defined.")
	String title,
	
	@NotBlank(message="The book author must be defined.")
	String author,
	
	@NotNull(message="The book price must be defined.")
	@Positive(message="The book price must be greater than zero.")
	Double price
) {}
