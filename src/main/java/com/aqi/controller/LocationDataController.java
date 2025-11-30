package com.aqi.controller;

import com.aqi.dto.location.LocationAirQualityHistoryData;
import com.aqi.dto.location.LocationClimateData;
import com.aqi.dto.location.LocationClimateSummaryData;
import com.aqi.dto.location.MapLocationData;
import com.aqi.service.OpenMeteoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class LocationDataController {

    private final OpenMeteoService openMeteoService;

    @GetMapping
    public LocationClimateData getLocationClimateData(@RequestParam Double latitude, @RequestParam Double longitude) {
        return openMeteoService.getLocationClimateData(latitude, longitude);
    }

    @GetMapping("/summary")
    public LocationClimateSummaryData getLocationClimateSummaryData(@RequestParam Double latitude, @RequestParam Double longitude) {
        return openMeteoService.getLocationClimateSummaryData(latitude, longitude);
    }

    @GetMapping("/history")
    public LocationAirQualityHistoryData getLocationAQHistoryData(@RequestParam Double latitude, @RequestParam Double longitude) {
        return openMeteoService.getLocationAQHistoryData(latitude, longitude);
    }

    @GetMapping("/map")
    public List<MapLocationData> getMapData(@RequestParam("bounding_box") List<Double> boundingBox, @RequestParam(name = "grid_resolution", defaultValue = "10") Integer gridResolution) {
        return openMeteoService.getMapLocations(boundingBox, gridResolution);
    }
}
