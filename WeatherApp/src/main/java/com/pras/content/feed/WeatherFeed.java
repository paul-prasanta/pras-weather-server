package com.pras.content.feed;

/**
 * Data handling object as per Weather Provider API schema
 * 
 * @see https://www.weatherapi.com/api-explorer.aspx
 * @author Prasanta
 *
 */
public class WeatherFeed {

	public static class Location {
		private String name;
		private String region;
		private String country;
		private Double lat;
		private Double lon;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
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
		public Double getLat() {
			return lat;
		}
		public void setLat(Double lat) {
			this.lat = lat;
		}
		public Double getLon() {
			return lon;
		}
		public void setLon(Double lon) {
			this.lon = lon;
		}
		
		@Override
		public String toString() {
			return "Location [name=" + name + ", region=" + region + ", country=" + country + ", lat=" + lat + ", lon="
					+ lon + "]";
		}
	}
	
	public static class Current {
		private Double temp_c;
		private Double temp_f;
		private Condition condition;
		private Double wind_kph;
		private Double wind_mph;
		private String wind_dir;
		private Integer humidity;
		private Integer cloud;
		private Double feelslike_c;
		private Double feelslike_f;
		
		public Double getTemp_c() {
			return temp_c;
		}
		public void setTemp_c(Double temp_c) {
			this.temp_c = temp_c;
		}
		public Double getTemp_f() {
			return temp_f;
		}
		public void setTemp_f(Double temp_f) {
			this.temp_f = temp_f;
		}
		public Condition getCondition() {
			return condition;
		}
		public void setCondition(Condition condition) {
			this.condition = condition;
		}
		public Double getWind_kph() {
			return wind_kph;
		}
		public void setWind_kph(Double wind_kph) {
			this.wind_kph = wind_kph;
		}
		public Double getWind_mph() {
			return wind_mph;
		}
		public void setWind_mph(Double wind_mph) {
			this.wind_mph = wind_mph;
		}
		public String getWind_dir() {
			return wind_dir;
		}
		public void setWind_dir(String wind_dir) {
			this.wind_dir = wind_dir;
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
		public Double getFeelslike_c() {
			return feelslike_c;
		}
		public void setFeelslike_c(Double feelslike_c) {
			this.feelslike_c = feelslike_c;
		}
		public Double getFeelslike_f() {
			return feelslike_f;
		}
		public void setFeelslike_f(Double feelslike_f) {
			this.feelslike_f = feelslike_f;
		}
	}
	
	public static class Condition {
		private String text;
		private String icon;
		
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		@Override
		public String toString() {
			return "Condition [text=" + text + ", icon=" + icon + "]";
		}
	}
	
	private Location location;
	private Current current;
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Current getCurrent() {
		return current;
	}
	public void setCurrent(Current current) {
		this.current = current;
	}
	
	@Override
	public String toString() {
		return "WeatherFeed [location=" + location + ", current=" + current + "]";
	}
}
