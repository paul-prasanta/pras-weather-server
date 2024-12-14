package com.pras;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import com.pras.utils.Exceptions.ErrorCode;

/**
 * Unit Tests for Weather History API - @WeatherController
 * 
 * @author Prasanta
 *
 */
@AutoConfigureMockMvc
@SpringBootTest
public class WeatherHistoryApiTest {

	final String WEATHER_HISTROY_API = "/api/weather/history";
	
	@Autowired
    private MockMvc mockMvc;
	@MockitoBean
	private WeatherService weatherService; // Mock Weather Service
	
	@Test
	public void testApiFailureWithoutParameters() throws Exception {
		mockMvc.perform(get(WEATHER_HISTROY_API)).andDo(print()).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testApiSuccessWithoutLocation() throws Exception {
		mockMvc.perform(get(WEATHER_HISTROY_API + "?user=Rito")).andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testApiSuccessWithoutUser() throws Exception {
		mockMvc.perform(get(WEATHER_HISTROY_API + "?location=Space")).andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testApiSuccessWithBothParameters() throws Exception {
		mockMvc.perform(get(WEATHER_HISTROY_API + "?user=Rito&location=Space")).andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testNoHistory() throws Exception {
		when(weatherService.getHistory("Rito", "Space")).thenReturn(null);
		
		mockMvc.perform(get(WEATHER_HISTROY_API + "?user=Rito&location=Space"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string("[]"));
	}
	
	@Test
	public void testInactiveAccount() throws Exception {
		when(weatherService.getHistory("Rito", "Space")).thenThrow(new AccountAccessException("Inactive user"));
		
		mockMvc.perform(get(WEATHER_HISTROY_API + "?user=Rito&location=Space"))
		.andDo(print())
		.andExpect(status().isInternalServerError())
		.andExpect(jsonPath("$.error_label", is(ErrorCode.ACCESS_DENIED.name())))
		.andExpect(jsonPath("$.message", is("Inactive user")));
	}
	
	@Test
	public void testHistoryEntries() throws Exception {
		// Dummy weather history (testing important fields)
		WeatherDto wd1 = new WeatherDto();
		Location location = new Location();
		location.setCity("Bengaluru");
		location.setRegion("Karnataka");
		location.setCountry("India");
		location.setLatitude(10.12);
		location.setLongitude(-15.80);
		
		wd1.setLocation(location);
		wd1.setTemperatureC(12.2);
		wd1.setTemperatureF(54.0);
		wd1.setCondition("Partly cloudy");
		// Other dummy data
		List<WeatherDto> wds = new ArrayList<WeatherDto>();
		wds.add(wd1);
		
		when(weatherService.getHistory("Rito", "Space")).thenReturn(wds);
		
		mockMvc.perform(get(WEATHER_HISTROY_API + "?user=Rito&location=Space"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].location.city", is("Bengaluru")))
		.andExpect(jsonPath("$[0].location.country", is("India")))
		.andExpect(jsonPath("$[0].location.latitude", is(10.12)))
		.andExpect(jsonPath("$[0].location.longitude", is(-15.80)))
		.andExpect(jsonPath("$[0].temperature_c", is(12.2)))
		.andExpect(jsonPath("$[0].temperature_f", is(54.0)))
		.andExpect(jsonPath("$[0].condition", is("Partly cloudy")));
	}
}
