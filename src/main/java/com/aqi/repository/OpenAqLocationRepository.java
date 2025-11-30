package com.aqi.repository;

import com.aqi.dto.openaq.ClusterProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.aqi.entity.OpenAqLocation; // Assuming you have this entity
import java.util.List;

@Repository
public interface OpenAqLocationRepository extends JpaRepository<OpenAqLocation, Long> {

    /**
     * Clusters points based on a grid size.
     * * Logic:
     * 1. ST_MakeEnvelope: Creates the bounding box.
     * 2. &&: Efficiently finds points overlapping the box (using the spatial index).
     * 3. ST_SnapToGrid: Rounds coordinates to the nearest 'gridSize', effectively grouping them.
     * 4. ST_Centroid: Finds the visual center of the cluster.
     */
    @Query(value = """
        SELECT 
            count(*) as point_count,
            ST_Y(ST_Centroid(ST_Collect(location::geometry))) as lat,
            ST_X(ST_Centroid(ST_Collect(location::geometry))) as lon
        FROM openaq_locations 
        WHERE location && ST_MakeEnvelope(:minLon, :minLat, :maxLon, :maxLat, 4326)
        GROUP BY ST_SnapToGrid(location::geometry, :gridSize)
        """, nativeQuery = true)
    List<ClusterProjection> findClusters(
            @Param("minLon") double minLon,
            @Param("minLat") double minLat,
            @Param("maxLon") double maxLon,
            @Param("maxLat") double maxLat,
            @Param("gridSize") double gridSize
    );
}
