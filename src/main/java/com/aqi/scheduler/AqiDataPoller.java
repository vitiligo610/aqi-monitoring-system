package com.aqi.scheduler;

import com.aqi.entity.AqiDataPoint;
import com.aqi.repository.AqiDataPointRepository;
import com.aqi.repository.UserRepository;
import com.aqi.service.NotificationService;
import com.aqi.service.OpenWeatherApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AqiDataPoller {
    
    @Autowired
    private OpenWeatherApiClient openWeatherApiClient;
    
    @Autowired
    private AqiDataPointRepository aqiDataPointRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NotificationService notificationService;

    // Poll every hour (3600000 milliseconds)
    @Scheduled(fixedRate = 3600000)
    public void pollAqiData() {
        // Get all users with location data
        List<com.aqi.entity.User> users = userRepository.findAll();
        
        for (com.aqi.entity.User user : users) {
            if (user.getLatitude() != null && user.getLongitude() != null) {
                try {
                    // Fetch AQI for user's location
                    Double aqiValue = openWeatherApiClient.getAqiForCoordinates(
                            user.getLatitude(), 
                            user.getLongitude()
                    );
                    
                    if (aqiValue != null) {
                        // Determine city name (you might want to store this in User entity)
                        String city = "Location"; // Simplified - you should store city name
                        
                        // Save to database
                        AqiDataPoint dataPoint = new AqiDataPoint();
                        dataPoint.setCity(city);
                        dataPoint.setAqiValue(aqiValue);
                        aqiDataPointRepository.save(dataPoint);
                        
                        // Send notification if AQI is high (e.g., > 150)
                        if (aqiValue > 150) {
                            notificationService.sendAqiAlert(user.getEmail(), city, aqiValue);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error polling AQI data for user " + user.getEmail() + ": " + e.getMessage());
                }
            }
        }
    }
}

