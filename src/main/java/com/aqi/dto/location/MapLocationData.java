package com.aqi.dto.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MapLocationData {

    private Double latitude;
    private Double longitude;

    @JsonProperty("utc_offset_seconds")
    private Integer utcOffsetSeconds;

    private Integer aqi;

    @JsonProperty("is_cluster")
    private boolean isCluster;

    @JsonProperty("point_count")
    private Integer pointCount;
}
