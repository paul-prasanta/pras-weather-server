package com.pras;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.pras.content.WeatherService;
import com.pras.content.dto.WeatherDto;
import com.pras.content.dto.WeatherDto.Location;
import com.pras.utils.Exceptions.AccountAccessException;
import com.pras.utils.Exceptions.ApiException;
import com.pras.utils.Exceptions.ErrorCode;
import com.pras.utils.Exceptions.RecordNotFoundException;

/**
 * Unit Tests for Get Current Weather API - @WeatherController 
 * 
 * @author Prasanta
 *
 */
@AutoConfigureMockMvc
@SpringBootTest
public class WeatherApiTest {

	final String WEATHER_API = "/api/weather";
	
	@Autowired
    private MockMvc mockMvc;
	@MockitoBean
	private WeatherService weatherService; // Mock Weather Service
	
	@Test
	public void testApiFailureWithoutParameters() throws Exception {
		mockMvc.perform(get(WEATHER_API)).andDo(print()).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiFailureWithoutLocation() throws Exception {
		mockMvc.perform(get(WEATHER_API + "?user=Rito")).andDo(print()).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiFailureWithoutUser() throws Exception {
		mockMvc.perform(get(WEATHER_API + "?location=Space")).andDo(print()).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiSuccessWithBothParameters() throws Exception {
		mockMvc.perform(get(WEATHER_API + "?user=Lio&location=London")).andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testInactiveAccount() throws Exception {
		// User account is deactivated
		when(weatherService.getCurrentWeather("Rito", "Space")).thenThrow(new AccountAccessException("Inactive user"));
		
		mockMvc.perform(get(WEATHER_API + "?user=Rito&location=Space"))
		.andDo(print())
		.andExpect(status().isInternalServerError())
		.andExpect(jsonPath("$.error_label", is(ErrorCode.ACCESS_DENIED.name())))
		.andExpect(jsonPath("$.message", is("Inactive user")));
	}
	
	@Test
	public void testWeatherProviderApiError() throws Exception {
		// Weather Provider API access issues
		when(weatherService.getCurrentWeather("Rito", "Space")).thenThrow(new ApiException("Api error"));
		
		mockMvc.perform(get(WEATHER_API + "?user=Rito&location=Space"))
		.andDo(print())
		.andExpect(status().isInternalServerError())
		.andExpect(jsonPath("$.error_label", is(ErrorCode.API_ERROR.name())))
		.andExpect(jsonPath("$.message", is("Api error")));
	}
	
	@Test
	public void testNoWeatherRecordFoundError() throws Exception {
		// No current weather information
		when(weatherService.getCurrentWeather("Rito", "Space")).thenThrow(new RecordNotFoundException("No record"));
		
		mockMvc.perform(get(WEATHER_API + "?user=Rito&location=Space"))
		.andDo(print())
		.andExpect(status().isInternalServerError())
		.andExpect(jsonPath("$.error_label", is(ErrorCode.NO_RECORD_FOUND.name())))
		.andExpect(jsonPath("$.message", is("No record")));
	}
	
	@Test
	public void testWeatherEntry() throws Exception {
		// Dummy current weather
		WeatherDto wd = new WeatherDto();
		Location location = new Location();
		location.setCity("Bengaluru");
		location.setRegion("Karnataka");
		location.setCountry("India");
		location.setLatitude(10.12);
		location.setLongitude(-15.80);
		
		wd.setLocation(location);
		wd.setTemperatureC(12.2);
		wd.setTemperatureF(54.0);
		wd.setCondition("Partly cloudy");
		
		when(weatherService.getCurrentWeather("Rito", "Space")).thenReturn(wd);
		
		mockMvc.perform(get(WEATHER_API + "?user=Rito&location=Space"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.location.city", is("Bengaluru")))
		.andExpect(jsonPath("$.location.country", is("India")))
		.andExpect(jsonPath("$.location.latitude", is(10.12)))
		.andExpect(jsonPath("$.location.longitude", is(-15.80)))
		.andExpect(jsonPath("$.temperature_c", is(12.2)))
		.andExpect(jsonPath("$.temperature_f", is(54.0)))
		.andExpect(jsonPath("$.condition", is("Partly cloudy")));
	}
}
