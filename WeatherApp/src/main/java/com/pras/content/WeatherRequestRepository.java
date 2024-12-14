package com.pras.content;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pras.account.User;

public interface WeatherRequestRepository extends JpaRepository<WeatherRequest, Integer> {

	public List<WeatherRequest> findByUser(User user);
	
	public List<WeatherRequest> findByLocation(String location);
	
	public List<WeatherRequest> findByUserAndLocation(User user, String location);
}
