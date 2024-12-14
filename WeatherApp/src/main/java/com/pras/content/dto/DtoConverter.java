package com.pras.content.dto;

import org.modelmapper.ModelMapper;

import com.pras.content.Weather;
import com.pras.content.dto.WeatherDto.Location;

public class DtoConverter {
	
	/**
	 * Convert Weather model record to data format for end clients
	 * 
	 * @param weather
	 * @return
	 */
	public WeatherDto convert(Weather weather) {
		ModelMapper modelMapper = new ModelMapper();
		Location location = modelMapper.map(weather, Location.class);
		WeatherDto weatherDto = modelMapper.map(weather, WeatherDto.class);
		weatherDto.setLocation(location);
		return weatherDto;
	}
}
