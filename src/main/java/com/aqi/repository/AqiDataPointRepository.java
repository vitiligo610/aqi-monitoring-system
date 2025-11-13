package com.aqi.repository;

import com.aqi.entity.AqiDataPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AqiDataPointRepository extends JpaRepository<AqiDataPoint, Long> {
    List<AqiDataPoint> findByCityOrderByTimestampDesc(String city);
    
    @Query("SELECT a FROM AqiDataPoint a WHERE a.city = :city AND a.timestamp >= :startTime ORDER BY a.timestamp DESC")
    List<AqiDataPoint> findByCityAndTimestampAfter(@Param("city") String city, @Param("startTime") LocalDateTime startTime);
    
    AqiDataPoint findFirstByCityOrderByTimestampDesc(String city);
}

