package com.pras;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.pras.account.User;
import com.pras.account.UserService;
import com.pras.content.Weather;
import com.pras.content.WeatherRepository;
import com.pras.content.WeatherRequest;
import com.pras.content.WeatherRequest.Status;
import com.pras.content.WeatherRequestRepository;
import com.pras.content.WeatherService;
import com.pras.content.dto.WeatherDto;
import com.pras.content.dto.WeatherDto.Location;
import com.pras.content.feed.WeatherFeedService;
import com.pras.utils.Exceptions.AccountAccessException;
import com.pras.utils.Exceptions.ApiException;
import com.pras.utils.Exceptions.RecordNotFoundException;

/**
 * Unit Tests for @WeatherService
 * 
 * @author Prasanta
 *
 */
@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

	@InjectMocks
	private WeatherService weatherService;
	
	@Mock
	private WeatherRequestRepository weatherRequestRepository;
	@Mock
	private WeatherRepository weatherRepository;
	@Mock
	private WeatherFeedService weatherFeedService;
	@Mock
	private UserService userService;
	
	@BeforeEach
	public void setup() {
		// Mock API URL and Key Fields
		ReflectionTestUtils.setField(weatherService, "WEATHER_PROVIDER", "Provider");
	}
	
	@Test
	public void testGetWeatherForInActiveUser() {
		User user = new User();
		user.setUserName("Rito");
		user.setActive(true);
		
		// Mock user register call
		when(userService.register("Rito")).thenReturn(user);
		
		// Mock user account check call and Throw exception
		doThrow(new AccountAccessException("Inactive user")).when(userService).checkAccess(user);
		
		assertThrows(AccountAccessException.class, () -> {
			weatherService.getCurrentWeather("Rito", "Space");
		});
	}
	
	@Test
	public void testGetWeatherWithApiFailure() {
		// Mock api call to read weather
		when(weatherFeedService.readCurrentWeather("Space")).thenThrow(new ApiException("Api error"));
		
		WeatherRequest request = new WeatherRequest();
		request.setLocation("Space");
		request.setStatus(Status.OPEN);
		
		// Mock Request save by returning mentioned instance
		when(weatherRequestRepository.save(Mockito.any(WeatherRequest.class))).thenReturn(request);
		
		// It should throw API Exception
		assertThrows(ApiException.class, () -> {
			weatherService.getCurrentWeather("Rito", "Space");
		});
		
		// Request status should change to Error
		assertThat(request.getStatus()).isEqualTo(Status.ERROR);
	}
	
	@Test
	public void testGetWeatherWithApiFeed() {
		// Mock api call to return weather
		Weather weather = new Weather();
		weather.setCity("Bengaluru");
		weather.setRegion("Karnataka");
		weather.setCountry("India");
		weather.setLatitude(10.12);
		weather.setLongitude(-15.80);
		weather.setTemperatureC(12.2);
		weather.setTemperatureF(54.0);
		weather.setTemperatureFeelsC(11.4);
		weather.setTemperatureFeelsF(52.4);
		weather.setWindKph(9.4);
		weather.setWindMph(5.8);
		weather.setWindDirection("N");
		weather.setCloud(75);
		weather.setHumidity(15);
		weather.setCondition("Partly cloudy");
		weather.setConditionIcon("https://img.io/w.png");
		when(weatherFeedService.readCurrentWeather("Space")).thenReturn(weather);
		
		WeatherRequest request = new WeatherRequest();
		request.setLocation("Space");
		request.setStatus(Status.OPEN);
		
		// Mock Request save by returning mentioned instance
		when(weatherRequestRepository.save(Mockito.any(WeatherRequest.class))).thenReturn(request);
		
		// Mock Weather save by returning mentioned instance
		when(weatherRepository.save(Mockito.any(Weather.class))).thenReturn(weather);
		
		// Expect weather record 
		WeatherDto weatherDto = weatherService.getCurrentWeather("Rito", "Space");
		Location location = weatherDto.getLocation();
		
		// Request status should change to Error
		assertThat(location.getCity()).isEqualTo("Bengaluru");
		assertThat(location.getRegion()).isEqualTo("Karnataka");
		assertThat(location.getCountry()).isEqualTo("India");
		assertThat(location.getLatitude()).isEqualTo(10.12);
		assertThat(location.getLongitude()).isEqualTo(-15.80);
		assertThat(weatherDto.getTemperatureC()).isEqualTo(12.2);
		assertThat(weatherDto.getTemperatureF()).isEqualTo(54.0);
		assertThat(weatherDto.getTemperatureFeelsC()).isEqualTo(11.4);
		assertThat(weatherDto.getTemperatureFeelsF()).isEqualTo(52.4);
		assertThat(weatherDto.getWindKph()).isEqualTo(9.4);
		assertThat(weatherDto.getWindMph()).isEqualTo(5.8);
		assertThat(weatherDto.getWindDirection()).isEqualTo("N");
		assertThat(weatherDto.getCloud()).isEqualTo(75);
		assertThat(weatherDto.getHumidity()).isEqualTo(15);
		assertThat(weatherDto.getCondition()).isEqualTo("Partly cloudy");
		assertThat(weatherDto.getConditionIcon()).isEqualTo("https://img.io/w.png");
	}
	
	@Test
	public void testGetWeatherWithNoApiFeed() {
		// Mock api call to return weather
		Weather weather = new Weather();
		when(weatherFeedService.readCurrentWeather("Space")).thenReturn(weather);
		
		WeatherRequest request = new WeatherRequest();
		request.setLocation("Space");
		request.setStatus(Status.OPEN);
		
		// Mock Request save by returning mentioned instance
		when(weatherRequestRepository.save(Mockito.any(WeatherRequest.class))).thenReturn(request);
		
		// Expect RecordNotFoundException
		assertThrows(RecordNotFoundException.class, () -> {
			weatherService.getCurrentWeather("Rito", "Space");
		});
		
		// Request status should change to Error
		assertThat(request.getStatus()).isEqualTo(Status.ERROR);
	}
	
	@Test
	public void testGetHistoryForInActiveUser() {
		User user = new User();
		user.setUserName("Rito");
		user.setActive(true);
		
		// Mock user register call
		when(userService.register("Rito")).thenReturn(user);
		
		// Mock user account check call and Throw exception
		doThrow(new AccountAccessException("Inactive user")).when(userService).checkAccess(user);
		
		assertThrows(AccountAccessException.class, () -> {
			weatherService.getHistory("Rito", "Space");
		});
	}
	
	@Test
	public void testGetHistoryWithUserAndRecords() {
		final String userName = "Rito";
		final String location = null;
		
		User user = new User();
		user.setUserName(userName);
		user.setActive(true);
		
		// Mock user register call
		when(userService.register(userName)).thenReturn(user);
		
		// Mock weather request query
		WeatherRequest request = new WeatherRequest();
		request.setLocation("Space");
		request.setStatus(Status.OPEN);
		request.setUser(user);
		List<WeatherRequest> requests = new ArrayList<WeatherRequest>();
		when(weatherRequestRepository.findByUser(user)).thenReturn(requests);
		
		Weather w1 = new Weather();
		w1.setCity("Bengaluru");
		w1.setCountry("India");
		w1.setTemperatureC(22.0);
		w1.setTemperatureF(71.0);
		w1.setCondition("Partly cloudy");
		
		List<Weather> ws = new ArrayList<Weather>();
		ws.add(w1);
		
		// Mock weather history records
		when(weatherRepository.findByWeatherRequestIn(requests)).thenReturn(ws);
		
		List<WeatherDto> wds = weatherService.getHistory(userName, location);
		
		assertThat(wds).isNotNull();
		assertThat(wds.get(0).getLocation().getCity()).isEqualTo("Bengaluru");
		assertThat(wds.get(0).getLocation().getCountry()).isEqualTo("India");
		assertThat(wds.get(0).getTemperatureC()).isEqualTo(22.0);
		assertThat(wds.get(0).getTemperatureF()).isEqualTo(71.0);
		assertThat(wds.get(0).getCondition()).isEqualTo("Partly cloudy");
	}
	
	@Test
	public void testGetHistoryWithLocationAndRecords() {
		final String userName = null;
		final String location = "Space";
		
		// Mock weather request query
		WeatherRequest request = new WeatherRequest();
		request.setLocation("Space");
		request.setStatus(Status.OPEN);
		List<WeatherRequest> requests = new ArrayList<WeatherRequest>();
		when(weatherRequestRepository.findByLocation(location)).thenReturn(requests);
		
		Weather w1 = new Weather();
		w1.setCity("Bengaluru");
		w1.setCountry("India");
		w1.setTemperatureC(22.0);
		w1.setTemperatureF(71.0);
		w1.setCondition("Partly cloudy");
		
		List<Weather> ws = new ArrayList<Weather>();
		ws.add(w1);
		
		// Mock weather history records
		when(weatherRepository.findByWeatherRequestIn(requests)).thenReturn(ws);
		
		List<WeatherDto> wds = weatherService.getHistory(userName, location);
		
		assertThat(wds).isNotNull();
		assertThat(wds.get(0).getLocation().getCity()).isEqualTo("Bengaluru");
		assertThat(wds.get(0).getLocation().getCountry()).isEqualTo("India");
		assertThat(wds.get(0).getTemperatureC()).isEqualTo(22.0);
		assertThat(wds.get(0).getTemperatureF()).isEqualTo(71.0);
		assertThat(wds.get(0).getCondition()).isEqualTo("Partly cloudy");
	}
	
	@Test
	public void testGetHistoryWithUserAndLocationAndRecords() {
		final String userName = "Rito";
		final String location = "Space";
		
		User user = new User();
		user.setUserName(userName);
		user.setActive(true);
		
		// Mock user register call
		when(userService.register(userName)).thenReturn(user);
		
		// Mock weather request query
		WeatherRequest request = new WeatherRequest();
		request.setLocation("Space");
		request.setStatus(Status.OPEN);
		request.setUser(user);
		List<WeatherRequest> requests = new ArrayList<WeatherRequest>();
		when(weatherRequestRepository.findByUserAndLocation(user, location)).thenReturn(requests);
		
		Weather w1 = new Weather();
		w1.setCity("Bengaluru");
		w1.setCountry("India");
		w1.setTemperatureC(22.0);
		w1.setTemperatureF(71.0);
		w1.setCondition("Partly cloudy");
		
		List<Weather> ws = new ArrayList<Weather>();
		ws.add(w1);
		
		// Mock weather history records
		when(weatherRepository.findByWeatherRequestIn(requests)).thenReturn(ws);
		
		List<WeatherDto> wds = weatherService.getHistory(userName, location);
		
		assertThat(wds).isNotNull();
		assertThat(wds.get(0).getLocation().getCity()).isEqualTo("Bengaluru");
		assertThat(wds.get(0).getLocation().getCountry()).isEqualTo("India");
		assertThat(wds.get(0).getTemperatureC()).isEqualTo(22.0);
		assertThat(wds.get(0).getTemperatureF()).isEqualTo(71.0);
		assertThat(wds.get(0).getCondition()).isEqualTo("Partly cloudy");
	}
	
	@Test
	public void testGetHistoryWithNoPreviousRequests() {
		final String userName = "Rito";
		final String location = "Space";
		
		User user = new User();
		user.setUserName(userName);
		user.setActive(true);
		
		// Mock user register call
		when(userService.register(userName)).thenReturn(user);
		
		// Mock weather request query
		when(weatherRequestRepository.findByUserAndLocation(user, location)).thenReturn(null);
		
		List<WeatherDto> wds = weatherService.getHistory(userName, location);
		
		assertThat(wds).isNull();
	}
	
	@Test
	public void testGetHistoryWithPreviousRequestAndNoMatchingWeather() {
		final String userName = "Rito";
		final String location = "Space";
		
		User user = new User();
		user.setUserName(userName);
		user.setActive(true);
		
		// Mock user register call
		when(userService.register(userName)).thenReturn(user);
		
		// Mock weather request query
		WeatherRequest request = new WeatherRequest();
		request.setLocation("Space");
		request.setStatus(Status.OPEN);
		request.setUser(user);
		List<WeatherRequest> requests = new ArrayList<WeatherRequest>();
		when(weatherRequestRepository.findByUserAndLocation(user, location)).thenReturn(requests);
		
		List<Weather> ws = new ArrayList<Weather>();
		
		// Mock weather history query with empty result
		when(weatherRepository.findByWeatherRequestIn(requests)).thenReturn(ws);
		
		List<WeatherDto> wds = weatherService.getHistory(userName, location);
		
		assertThat(wds).isNull();
	}
}
