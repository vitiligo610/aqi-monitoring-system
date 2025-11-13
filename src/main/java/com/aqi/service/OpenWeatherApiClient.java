package com.aqi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class OpenWeatherApiClient {
    
    @Value("${app.openweathermap.api-key}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/air_pollution";
    
    public OpenWeatherApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public Double getAqiForCity(String city) {
        // Note: OpenWeatherMap Air Pollution API requires lat/lon, not city name
        // This is a simplified version. In production, you'd need to geocode the city first
        // For now, returning a mock value. You should implement proper geocoding.
        try {
            // Example: You would need to get lat/lon from city name first
            // For now, returning null to indicate we need coordinates
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch AQI data from OpenWeatherMap API", e);
        }
    }

    @SuppressWarnings("unchecked")
    public Double getAqiForCoordinates(Double lat, Double lon) {
        try {
            String url = String.format("%s?lat=%s&lon=%s&appid=%s", BASE_URL, lat, lon, apiKey);
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("list") && ((java.util.List<?>) response.get("list")).size() > 0) {
                Map<String, Object> main = (Map<String, Object>) ((java.util.List<?>) response.get("list")).get(0);
                // OpenWeatherMap returns AQI on a scale of 1-5, we'll convert it to a 0-500 scale
                Integer aqi = (Integer) main.get("aqi");
                if (aqi != null) {
                    // Convert 1-5 scale to 0-500 scale (approximate)
                    return aqi * 100.0;
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch AQI data from OpenWeatherMap API", e);
        }
    }
}

