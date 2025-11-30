package com.aqi.service;

import com.aqi.dto.openaq.ClusterProjection;
import com.aqi.repository.OpenAqLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAqService {

    private final OpenAqLocationRepository locationRepository;
    private static final double DEFAULT_GRID_RESOLUTION = 15.0;
    private static final double MIN_GRID_SIZE = 0.0005;

    /**
     * Clusters locations within a bounding box using PostGIS grid logic.
     */
    public List<ClusterProjection> getClustersInBoundingBox(List<Double> bbox, Integer gridResolution) {
        if (bbox == null || bbox.size() != 4) {
            log.warn("Invalid bounding box provided: {}", bbox);
            return Collections.emptyList();
        }

        double swLat = bbox.get(0);
        double swLon = bbox.get(1);
        double neLat = bbox.get(2);
        double neLon = bbox.get(3);

        double gridSize = calculateGridSize(swLat, swLon, neLat, neLon, gridResolution);

        log.info("Querying clusters for bbox: {} with grid size: {}", bbox, gridSize);

        return locationRepository.findClusters(swLon, swLat, neLon, neLat, gridSize);
    }

    /**
     * Calculates the dynamic grid cell size (in degrees) based on the viewport size.
     * * Logic: The wider the map view, the larger the grid cells must be to avoid
     * returning too many points.
     */
    private double calculateGridSize(double swLat, double swLon, double neLat, double neLon, Integer resolution) {
        double latDiff = Math.abs(neLat - swLat);
        double lonDiff = Math.abs(neLon - swLon);
        double maxDiff = Math.max(latDiff, lonDiff);

        double resolutionFactor = (resolution != null && resolution > 0) ? resolution : DEFAULT_GRID_RESOLUTION;

        double calculatedSize = maxDiff / resolutionFactor;

        return Math.max(calculatedSize, MIN_GRID_SIZE);
    }
}
