package com.polarbookshop.catalogservice.domain;
// ex 3.4 도메인 개체 정의
// ex 3.12 각 필드에 대해 정의한 유효성 제약

import java.time.Instant;

import javax.validation.constraints.NotBlank;		// gradle 2.7.18 버전 시
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

//import jakarta.validation.constraints.NotBlank;	// gradle 3.4.4 버전 시
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Positive;

/*  Record 타입 : 불변 데이터 객체를 간단하게 선언하는 문법
 * () 내부 : 생성자 파라미터 정의 -> 자동으로 필드, 생성자, getter, toString(), equals(), hashCode() 같은 메서드 생성
 * 이유 : "필드 = 생성자 파라미터"가 1:1로 대응됨
 * */
public record Book(
		
	@Id					// 이 필드를 엔티티에 대한 기본 키로 식별
	Long id,
		
	@NotBlank(message="The book ISBN must be defined.")
	@Pattern(regexp="^([0-9]{10}|[0-9]{13})$", message="The ISBN format must be valid.")
	String isbn,
	
	@NotBlank(message="The book title must be defined.")
	String title,
	
	@NotBlank(message="The book author must be defined.")
	String author,
	
	@NotNull(message="The book price must be defined.")
	@Positive(message="The book price must be greater than zero.")
	Double price,
	
	String publisher,	// 새로운 선택적 필드
	
	@CreatedDate		// 엔티티가 생성된 때
	Instant createdDate,
	
	@LastModifiedDate	// 엔티티가 마지막으로 수정된 때
	Instant lastModifiedDate,
	
	@Version			// 낙관적 잠금을 위해 사용되는 엔티티 버전 번호
	int version
) {
	
	public static Book of(String isbn, String title, String author, Double price, String publisher) {
		// ID가 null 이고 version 이 0이면 새로운 Entity 로 인식
		return new Book(null, isbn, title, author, price, publisher, null, null, 0);
	}
}
