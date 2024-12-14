package com.pras.content.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Weather data format for end clients
 * 
 * @author Prasanta
 *
 */
public class WeatherDto {

	public static class Location {
		private String city;
		private String region;
		private String country;
		private Double latitude;
		private Double longitude;
		
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public Double getLatitude() {
			return latitude;
		}
		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}
		public Double getLongitude() {
			return longitude;
		}
		public void setLongitude(Double longitude) {
			this.longitude = longitude;
		}
	}
	
	private Location location;
	private String condition;
	private String conditionIcon;
	@JsonProperty("temperature_c")
	private Double temperatureC;
	@JsonProperty("temperature_f")
	private Double temperatureF;
	@JsonProperty("temperature_feels_like_c")
	private Double temperatureFeelsC;
	@JsonProperty("temperature_feels_like_f")
	private Double temperatureFeelsF;
	private Integer humidity;
	private Integer cloud;
	@JsonProperty("wind_kph")
	private Double windKph;
	@JsonProperty("wind_mph")
	private Double windMph;
	private String windDirection;
	@JsonProperty("time")
	private Date creationTime;
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getConditionIcon() {
		return conditionIcon;
	}
	public void setConditionIcon(String conditionIcon) {
		this.conditionIcon = conditionIcon;
	}
	public Double getTemperatureC() {
		return temperatureC;
	}
	public void setTemperatureC(Double temperatureC) {
		this.temperatureC = temperatureC;
	}
	public Double getTemperatureF() {
		return temperatureF;
	}
	public void setTemperatureF(Double temperatureF) {
		this.temperatureF = temperatureF;
	}
	public Double getTemperatureFeelsC() {
		return temperatureFeelsC;
	}
	public void setTemperatureFeelsC(Double temperatureFeelsC) {
		this.temperatureFeelsC = temperatureFeelsC;
	}
	public Double getTemperatureFeelsF() {
		return temperatureFeelsF;
	}
	public void setTemperatureFeelsF(Double temperatureFeelsF) {
		this.temperatureFeelsF = temperatureFeelsF;
	}
	public Integer getHumidity() {
		return humidity;
	}
	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}
	public Integer getCloud() {
		return cloud;
	}
	public void setCloud(Integer cloud) {
		this.cloud = cloud;
	}
	public Double getWindKph() {
		return windKph;
	}
	public void setWindKph(Double windKph) {
		this.windKph = windKph;
	}
	public Double getWindMph() {
		return windMph;
	}
	public void setWindMph(Double windMph) {
		this.windMph = windMph;
	}
	public String getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
}
