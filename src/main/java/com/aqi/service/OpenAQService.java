package com.aqi.service;

import com.aqi.dto.omw.AirPollutionResponse;
import com.aqi.util.AqiCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class OpenAQService {
    private final AqiCalculator aqiCalculator;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.openaq.api-key}")
    private String apiKey;

    public AirPollutionResponse fetchAirPollutionData(Double lat, Double lon) {
        // TODO: Complete this method
    }

    private Map<String, Double> extractPollutants(AirPollutionResponse.Components components) {
     // TODO: Complete this method from OpenWeatherService
    }
}
