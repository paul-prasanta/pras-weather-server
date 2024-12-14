package com.pras.content;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pras.account.User;
import com.pras.account.UserService;
import com.pras.content.WeatherRequest.Status;
import com.pras.content.dto.DtoConverter;
import com.pras.content.dto.WeatherDto;
import com.pras.content.feed.WeatherFeedService;
import com.pras.utils.Exceptions.ApiException;
import com.pras.utils.Exceptions.RecordNotFoundException;

/**
 * Service to manage current weather requests and historic records
 * 
 * @author Prasanta
 *
 */
@Service
public class WeatherService {
	
	@Value("${com.pras.weather.api.provider}")
	private String WEATHER_PROVIDER;
	
	@Autowired
	private WeatherRequestRepository weatherRequestRepository;
	@Autowired
	private WeatherRepository weatherRepository;

	@Autowired
	private UserService userService;
	@Autowired
	private WeatherFeedService weatherFeedService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Place request to read current weather
	 * 
	 * @param userName
	 * @param location
	 * @return
	 */
	public WeatherDto getCurrentWeather(String userName, String location) {
		logger.info("Request Current Weather...{}, {}", userName, location);
		
		// Register or get existing user instance
		User user = userService.register(userName);
		userService.checkAccess(user);
		
		// Save Request instance
		WeatherRequest request = new WeatherRequest();
		request.setUser(user);
		request.setLocation(location);
		request.setProvider(WEATHER_PROVIDER);
		request.setStatus(Status.OPEN);
		
		request = weatherRequestRepository.save(request);
		
		// Read from Weather API
		Weather weather = null;
		
		try {
			weather = weatherFeedService.readCurrentWeather(location);
			
		} catch(Exception ex) {
			logger.error("Failed to read Weather: {}", ex.getMessage());
			request.setStatus(Status.ERROR);
			request.setNote(ex.getMessage());
			request = weatherRequestRepository.save(request);
			
			ex.printStackTrace();
			throw new ApiException(ex.getMessage());
		}
		
		if(weather == null || weather.getCity() == null || weather.getTemperatureC() == null) {
			// No record found
			String message = "No weather record found";
			logger.info(message);
			request.setStatus(Status.ERROR);
			request.setNote(message);
			request = weatherRequestRepository.save(request);
			
			throw new RecordNotFoundException(message);
		}
		
		// Success
		request.setStatus(Status.COMPLETE);
		request = weatherRequestRepository.save(request);
		
		weather.setWeatherRequest(request);
		weather = weatherRepository.save(weather);
		return new DtoConverter().convert(weather);
	}
	
	/**
	 * Get previously fetched weather records
	 * 
	 * @param userName
	 * @param location
	 * @return
	 */
	public List<WeatherDto> getHistory(String userName, String location) {
		logger.info("Get History...{}, {}", userName, location);
		User user = null;
		if(userName != null) {
			user = userService.register(userName);
			userService.checkAccess(user);
		}
		
		List<WeatherRequest> requests = null;
		
		if(user != null && location == null)
			requests = weatherRequestRepository.findByUser(user);
		else if(user == null && location != null)
			requests = weatherRequestRepository.findByLocation(location);
		else if(user != null && location != null)
			requests = weatherRequestRepository.findByUserAndLocation(user, location);
		
		if(requests == null) {
			// No entry found
			logger.info("No previous request found");
			return null;
		}
		
		List<Weather> weathers = weatherRepository.findByWeatherRequestIn(requests);
		
		if(weathers == null || weathers.size() == 0) {
			// No entry found
			logger.info("No weather history found");
			return null;
		}
		
		// DTO conversion
		List<WeatherDto> weatherDtos = weathers.stream().map((w) -> {
			return new DtoConverter().convert(w);
		}).collect(Collectors.toList());
		
		return weatherDtos;
	}
}
