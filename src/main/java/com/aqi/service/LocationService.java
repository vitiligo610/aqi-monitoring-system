package com.aqi.service;

import com.aqi.dto.location.LocationDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class LocationService {
    private final OpenWeatherMapService openWeatherMapService;
    private final OpenAQService openAQService;

    // TODO: Use the required methods for above services to consolidate their data
    public LocationDataDto getExternalLocationData(Double lat, Double lon) {

    }
}
