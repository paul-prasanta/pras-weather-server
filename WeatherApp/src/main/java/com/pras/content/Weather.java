package com.pras.content;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Weather model to store weather records fetched from Weather Provider web services. 
 * Each record is mapped to corresponding user request 
 * 
 * @See WeatherRequest
 * @author Prasanta
 *
 */
@Entity
@Table(name = "weather")
public class Weather {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "request_id")
	private WeatherRequest weatherRequest;
	
	private String city;
	private String region;
	private String country;
	private Double latitude;
	private Double longitude;
	@Column(name = "temperature_c")
	private Double temperatureC;
	
	@Column(name = "temperature_f")
	private Double temperatureF;
	
	@Column(name = "temperature_feels_c")
	private Double temperatureFeelsC;
	
	@Column(name = "temperature_feels_f")
	private Double temperatureFeelsF;
	
	private Integer humidity;
	private Integer cloud;
	
	@Column(name = "wind_kph")
	private Double windKph;
	
	@Column(name = "wind_mph")
	private Double windMph;
	
	@Column(name = "wind_direction")
	private String windDirection;
	
	@Column(name = "weather_condition")
	private String condition; // Partly Cloudy
	
	@Column(name = "weather_condition_icon")
	private String conditionIcon;
	
	private Date updateTime;
	private Date creationTime;
	
	@PrePersist
	public void onCreate() {
		Date now = new Date();
		if(this.getCreationTime() == null)
			setCreationTime(now);
		if(this.getUpdateTime() == null)
			setUpdateTime(now);
	}
	
	@PreUpdate
	public void onUpdate() {
		setUpdateTime(new Date());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WeatherRequest getWeatherRequest() {
		return weatherRequest;
	}

	public void setWeatherRequest(WeatherRequest weatherRequest) {
		this.weatherRequest = weatherRequest;
	}

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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public String toString() {
		return "Weather [id=" + id + ", weatherRequest=" + weatherRequest + ", city=" + city + ", region=" + region
				+ ", country=" + country + ", latitude=" + latitude + ", longitude=" + longitude + ", temperatureC="
				+ temperatureC + ", temperatureF=" + temperatureF + ", temperatureFeelsC=" + temperatureFeelsC
				+ ", temperatureFeelsF=" + temperatureFeelsF + ", humidity=" + humidity + ", cloud=" + cloud
				+ ", windKph=" + windKph + ", windMph=" + windMph + ", windDirection=" + windDirection + ", condition="
				+ condition + ", conditionIcon=" + conditionIcon + ", updateTime=" + updateTime + ", creationTime="
				+ creationTime + "]";
	}
}
