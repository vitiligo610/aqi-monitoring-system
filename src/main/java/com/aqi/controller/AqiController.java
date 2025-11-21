package com.aqi.controller;

import com.aqi.dto.location.LocationDataDto;
import com.aqi.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aqi")
public class AqiController {
    private final LocationService locationService;

    @GetMapping
    public LocationDataDto getAqi(@RequestParam Double lat, @RequestParam Double lon) {
        return locationService.getExternalLocationData(lat, lon);
    }
}