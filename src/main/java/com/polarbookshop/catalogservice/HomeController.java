package com.polarbookshop.catalogservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarbookshop.catalogservice.config.PolarProperties;

@RestController
public class HomeController {
	// 사용자 정의 속성 엑세스를 위해 생성자 auto wiring 을 통해 주입된 빈
	private final PolarProperties polarProperties;

	public HomeController(PolarProperties polarProperties) {
		this.polarProperties = polarProperties;
	}
	
	@GetMapping("/")
	public String getGreeting() {
		return polarProperties.getGreeting();
	}
}
