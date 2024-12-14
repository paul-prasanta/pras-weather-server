package com.pras;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pras.content.WeatherController;

@SpringBootTest
class WeatherAppApplicationTests {

	@Autowired
	WeatherController weatherController;
	
	@Test
	void contextLoads() {
		// Controller is initiated
		assertThat(weatherController).isNotNull();
	}
}
