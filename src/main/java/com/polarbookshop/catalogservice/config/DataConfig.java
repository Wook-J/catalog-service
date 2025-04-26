package com.polarbookshop.catalogservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@Configuration			// 이 클래스가 스프링 설정의 소스임을 나타냄
@EnableJdbcAuditing		// 지속성 엔티티에 대한 감사 활성화(데이터 생성, 변경, 삭제마다 감사 이벤트 생성)
public class DataConfig {

	/*
	 * Spring Data JDBC는 내부적으로 JdbcCustomConversions 빈을 참조하는데, 
	 * 슬라이스 테스트(@DataJdbcTest) 환경에서는 자동 등록이 안 됨.
	 * 그래서 명시적으로 빈 등록해줘야 테스트 통과함.
	 * -> 근데 이거 지워도 잘 돌아가는데?? 뭔가문제였던거지...
	 * */
//	@Bean
//	public JdbcCustomConversions jdbcCustomConversions() {
//		return new JdbcCustomConversions(List.of());
//	}
}
