package com.pras.content;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Integer> {

	public List<Weather> findByWeatherRequestIn(List<WeatherRequest> weatherRequest);
}
