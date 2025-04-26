package com.polarbookshop.catalogservice.web;
// ex 3.14 예외를 어떻게 처리할지 정의하는 advice Class

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.polarbookshop.catalogservice.domain.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.domain.BookNotFoundException;

/* 
 * @RestControllerAdvice : 클래스가 중앙식 예외 핸들러임을 표시
 * - @ControlerAdvice와 @ResponseBody의 조합
 * - 예외처리 메서드가 응답 본문에 데이터를 반환할 때 자동으로 JSON 형태로 변환
 * - @RestControllerAdvice 나 @ControllerAdvice 클래스 내 예외처리 메서드는 public 로 암묵적 가정 
 * */
@RestControllerAdvice
public class BookControllerAdvice {

	@ExceptionHandler(BookNotFoundException.class)	// 이 핸들러가 실행되어야 할 대상인 예외 정의
	@ResponseStatus(HttpStatus.NOT_FOUND)			// 404 찾을 수 없음
	String bookNotFoundHandler(BookNotFoundException ex) {
		// getMessage() : RuntimeException 클래스에서 이미 정의되어 있음
		// BookNotFoundException 클래스내 super(~~~)를 통해 그 message 가 전달됨
		return ex.getMessage();
	}
	
	@ExceptionHandler(BookAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)	// 422 처리할 수 없는 객체
	String bookAlreadyExistHandler(BookAlreadyExistsException ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)				// 400 잘못된 요청
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
		// 지역변수에는 var을 쓸 수 있음(Java 10부터).
		// 필드변수나 메서드의 매개변수 타입, 메서드 반환 타입에는 var 쓸 수 없음
		var errors = new HashMap<String, String>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();	// ObjectError를 구체적으로 FieldError로 변환(캐스팅)
			String errorMessage = error.getDefaultMessage();	// 유효성 검사에서 정의한 에러 메서지 가져옴
			errors.put(fieldName, errorMessage);				// 필드 이름과 메세지를 Map 에 저장
		});
		return errors;
	}
}
