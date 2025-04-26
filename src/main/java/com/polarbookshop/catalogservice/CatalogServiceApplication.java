package com.polarbookshop.catalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/*
 * @SpringBootApplication : 스프링 설정 클래스를 정의하고 컴포넌트 스캔과 스프링 부트 자동설정을 실행
 * - 아래의 3가지 어노테이션 포함
 * - @Configuration : 해당 클래스가 빈을 정의하는 클래스임을 나타냄
 * - @ComponentScan : 컴포넌트 검색을 통해 빈을 찾아 스프링 컨텍스트에 자동으로 등록
 * - @EnableAutoConfiguration : 스프링부트에서 제공하는 자동 설정 기능을 활성화
 * 
 * @ConfigurationPropertiesScan
 * - @ConfigurationProperties로 매핑할 클래스를 자동으로 검색해서 등록해주는 어노테이션
 * - application.yml 와 같은 설정파일에 있는 값을 자동으로 바인딩해주는 Bean 을 하나하나 등록하지 않고,
 *     패키지 전체를 스캔해서 등록하게 도와줌
 * */
@SpringBootApplication
@ConfigurationPropertiesScan
public class CatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogServiceApplication.class, args);
	}

}
