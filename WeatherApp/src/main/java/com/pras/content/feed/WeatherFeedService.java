package com.pras.content.feed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pras.content.Weather;
import com.pras.content.feed.WeatherFeed.Condition;
import com.pras.content.feed.WeatherFeed.Current;
import com.pras.content.feed.WeatherFeed.Location;

/**
 * Service to read live weather feed from Weather Provider (www.weatherapi.com). 
 * Need to create account and generate API Key.
 * 
 * @see https://www.weatherapi.com/api-explorer.aspx
 * @author Prasanta
 *
 */
@Service
public class WeatherFeedService {
	
	@Value("${com.pras.weather.api.url}")
	private String WEATHER_API_URL;
	
	@Value("${com.pras.weather.api.key}")
	private String WEATHER_API_KEY;
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	RestTemplate restTemplate;
	
	/**
	 * Read Current Weather using Provider API
	 * 
	 * @param location Location name (e.g. London) or Postal Code
	 * @return
	 */
	public Weather readCurrentWeather(String location) {
		String url = getUrl(location);
		logger.info("Weather API URL: {}", url);
		
		ResponseEntity<WeatherFeed> response = restTemplate.getForEntity(url, WeatherFeed.class);
		logger.info("API Response : {}", response.getStatusCode());
		
		WeatherFeed weatherFeed = response.getBody();
		logger.info("Weather API Response: {}", weatherFeed);
		
		Weather weather = convert(weatherFeed);
		logger.info("Weather : {}", weather);

		return weather;
	}
	
	private String getUrl(String location) {
		String URL_PATTERN = "%s?key=%s&q=%s";
		return String.format(URL_PATTERN, WEATHER_API_URL, WEATHER_API_KEY, location);
	}
	
	/**
	 * Convert Weather Feed to Entity
	 * 
	 * @param feed
	 * @return
	 */
	private Weather convert(WeatherFeed feed) {
		Weather weather = new Weather();
		
		Location location = feed.getLocation();
		Current current = feed.getCurrent();
		
		if(location == null && current == null)
			return null;
		
		if(location != null) {
			weather.setCity(location.getName());
			weather.setRegion(location.getRegion());
			weather.setCountry(location.getCountry());
			weather.setLatitude(location.getLat());
			weather.setLongitude(location.getLon());
		}
		
		if(current != null) {
			Condition condition = current.getCondition();
			
			if(condition != null) {
				weather.setCondition(condition.getText());
				weather.setConditionIcon(condition.getIcon());
			}
			
			weather.setTemperatureC(current.getTemp_c());
			weather.setTemperatureF(current.getTemp_f());
			weather.setTemperatureFeelsC(current.getFeelslike_c());
			weather.setTemperatureFeelsF(current.getFeelslike_f());
			weather.setHumidity(current.getHumidity());
			weather.setCloud(current.getCloud());
			weather.setWindKph(current.getWind_kph());
			weather.setWindMph(current.getWind_mph());
			weather.setWindDirection(current.getWind_dir());
		}
		
		return weather;
	}
}
