package com.pras;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.pras.content.Weather;
import com.pras.content.feed.WeatherFeed;
import com.pras.content.feed.WeatherFeed.Condition;
import com.pras.content.feed.WeatherFeed.Current;
import com.pras.content.feed.WeatherFeed.Location;
import com.pras.content.feed.WeatherFeedService;

/**
 * Units Tests for @WeatherFeedService
 * 
 * @author Prasanta
 *
 */
@ExtendWith(MockitoExtension.class)
public class WeatherFeedTest {

	private final String API_BASE_URL = "https://testweatherprovider.com/api";
	private final String API_KEY = "key01";
	private final String API_URL_FORMAT = "%s?key=%s&q=%s";
	
	@InjectMocks
	private WeatherFeedService weatherFeedService;
	@Mock
	private RestTemplate restTemplate;
	
	@BeforeEach
	public void apiSetup() {
		final String apiBaseUrl = "https://testweatherprovider.com/api";
		final String apiKey = "key01";
		
		// Mock API URL and Key Fields
		ReflectionTestUtils.setField(weatherFeedService, "WEATHER_API_URL", apiBaseUrl);
		ReflectionTestUtils.setField(weatherFeedService, "WEATHER_API_KEY", apiKey);
	}
	
	@Test
	public void testWeatherFeedApiSuccess() {
		final String locationParam = "560001";
		final String apiUrl = String.format(API_URL_FORMAT, API_BASE_URL, API_KEY, locationParam);
		
		// Return dummy feed (testing important fields)
		WeatherFeed feed = new WeatherFeed();
		Current current = new Current();
		Location location = new Location();
		
		location.setName("Bengaluru");
		location.setRegion("Karnataka");
		location.setCountry("India");
		location.setLat(10.12);
		location.setLon(-15.80);
		
		current.setTemp_c(12.2);
		current.setTemp_f(54.0);
		current.setFeelslike_c(11.4);
		current.setFeelslike_f(52.4);
		current.setWind_kph(9.4);
		current.setWind_mph(5.8);
		current.setWind_dir("N");
		current.setCloud(75);
		current.setHumidity(15);
		
		Condition condition = new Condition();
		condition.setText("Partly cloudy");
		condition.setIcon("https://img.io/w.png");
		
		current.setCondition(condition);
		feed.setCurrent(current);
		feed.setLocation(location);
		
		// Mock RestTemplate output
		when(restTemplate.getForEntity(apiUrl, WeatherFeed.class))
		.thenReturn(new ResponseEntity<WeatherFeed>(feed, HttpStatus.OK));
		
		Weather weather = weatherFeedService.readCurrentWeather(locationParam);
		
		assertThat(weather).isNotNull();
		assertThat(weather.getCity()).isEqualTo("Bengaluru");
		assertThat(weather.getRegion()).isEqualTo("Karnataka");
		assertThat(weather.getCountry()).isEqualTo("India");
		assertThat(weather.getLatitude()).isEqualTo(10.12);
		assertThat(weather.getLongitude()).isEqualTo(-15.80);
		assertThat(weather.getTemperatureC()).isEqualTo(12.2);
		assertThat(weather.getTemperatureF()).isEqualTo(54.0);
		assertThat(weather.getTemperatureFeelsC()).isEqualTo(11.4);
		assertThat(weather.getTemperatureFeelsF()).isEqualTo(52.4);
		assertThat(weather.getWindKph()).isEqualTo(9.4);
		assertThat(weather.getWindMph()).isEqualTo(5.8);
		assertThat(weather.getWindDirection()).isEqualTo("N");
		assertThat(weather.getCloud()).isEqualTo(75);
		assertThat(weather.getHumidity()).isEqualTo(15);
		assertThat(weather.getCondition()).isEqualTo("Partly cloudy");
		assertThat(weather.getConditionIcon()).isEqualTo("https://img.io/w.png");
	}
	
	@Test
	public void testWeatherFeedApiFailure() {
		final String locationParam = "560001";
		final String apiUrl = String.format(API_URL_FORMAT, API_BASE_URL, API_KEY, locationParam);
		
		// Mock RestTemplate output
		when(restTemplate.getForEntity(apiUrl, WeatherFeed.class))
		.thenThrow(new RestClientException("Api service is down"));
		
		assertThrows(RestClientException.class, () -> {
			weatherFeedService.readCurrentWeather(locationParam);
		});
	}
}
