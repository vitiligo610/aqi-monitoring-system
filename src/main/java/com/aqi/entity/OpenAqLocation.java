package com.aqi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "openaq_locations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAqLocation {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String timezone;

    @Column(name = "country_code", length = 4)
    private String countryCode;
}