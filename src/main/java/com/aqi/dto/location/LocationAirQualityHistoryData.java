package com.aqi.dto.location;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class LocationAirQualityHistoryData extends LocationClimateBaseData {

    private LocationAirQualityData.Forecast hourly;
    private LocationAirQualityData.Forecast daily;
}
