package com.aqi.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MapUtils {
    private static final Double MIN_DEGREE_STEP = 0.01;

    public record MapPointsResult(List<Double> latPoints, List<Double> lonPoints, int effectiveGridSize) { }

    public static MapPointsResult calculateSampledGridPoints(
            double swLat, double swLon, double neLat, double neLon, int samples
    ) {
        double latRange = neLat - swLat;
        double lonRange = neLon - swLon;

        int requestedGridSize = (int) Math.sqrt(samples);
        if (requestedGridSize < 2) requestedGridSize = 2;


        int maxStepsLat = (int) Math.floor(latRange / MIN_DEGREE_STEP) + 1;
        int maxStepsLon = (int) Math.floor(lonRange / MIN_DEGREE_STEP) + 1;

        int maxEffectiveGridSize = Math.min(maxStepsLat, maxStepsLon);

        int finalGridSize = Math.min(requestedGridSize, maxEffectiveGridSize);

        if (finalGridSize < 2 && (latRange > 0 || lonRange > 0)) {
            finalGridSize = 2;
        } else if (finalGridSize < 1) {
            finalGridSize = 1;
        }

        double latStep = (finalGridSize > 1) ? latRange / (finalGridSize - 1) : 0.0;
        double lonStep = (finalGridSize > 1) ? lonRange / (finalGridSize - 1) : 0.0;

        log.info(
                "Requested Grid: {}x{}. Effective Grid: {}x{}. Min Separation enforced.",
                requestedGridSize, requestedGridSize, finalGridSize, finalGridSize
        );

        List<Double> latPoints = new ArrayList<>();
        List<Double> lonPoints = new ArrayList<>();

        for (int i = 0; i < finalGridSize; i++) {
            for (int j = 0; j < finalGridSize; j++) {
                latPoints.add(swLat + (i * latStep));
                lonPoints.add(swLon + (j * lonStep));
            }
        }

        if (finalGridSize == 1 && latPoints.isEmpty()) {
            latPoints.add(swLat);
            lonPoints.add(swLon);
        }

        return new MapPointsResult(latPoints, lonPoints, finalGridSize);
    }
}
