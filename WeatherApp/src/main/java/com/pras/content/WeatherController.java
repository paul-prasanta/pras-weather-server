package com.pras.content;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pras.content.dto.WeatherDto;
import com.pras.utils.Exceptions.WeatherException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Weather App API Controller with options-
 * <br/>
 * - Read current weather using location name or postal code
 * <br/>
 * - Get history of previously searched weather records
 * <p>
 * User name is used for tracking. User account can be deactivated to block service access.
 * <p>
 * TODO: Apply user account security in v2
 * 
 * @author Prasanta
 *
 */
@RequestMapping("/api/weather")
@RestController
@Tag(name = "Weather API")
public class WeatherController {
	
	@Autowired
	WeatherService weatherService;
	
	/**
	 * Request current weather details for mentioned location
	 * 
	 * @param userName User Name. Registered or new User, account should be active
	 * @param location City name or Postal code
	 * @return
	 */
	@Operation(summary = "Get Current Weather", description = "Request current weather details for mentioned location (City name or Postal code)")
	@GetMapping("")
	public WeatherDto getWeather(@RequestParam("user") @Parameter(description = "Registered or new User, account should be active") String userName, 
			@RequestParam("location") @Parameter(description = "City name or Postal code") String location) {
		return weatherService.getCurrentWeather(userName, location);
	}
	
	/**
	 * Get history of weather records
	 * 
	 * @param userName Registered user name, account should be active
	 * @param location City Name or Postal Code used to place weather reading request
	 * @return
	 */
	@Operation(summary = "Get Weather history", description = "Get Weather history")
	@GetMapping("/history")
	public List<WeatherDto> getWeatherHistory(@RequestParam(name = "user", required = false) @Parameter(description = "Registered or new User, account should be active") String userName, 
			@RequestParam(name = "location", required = false) @Parameter(description = "City name or Postal code") String location) {
		if(userName == null && location == null)
			throw new WeatherException("Missing required parameter: User or Location");
		
		List<WeatherDto> weatherDtos = weatherService.getHistory(userName, location);
		return weatherDtos != null ? weatherDtos : new ArrayList<WeatherDto>();
	}
}
