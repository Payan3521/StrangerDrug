package com.desarrollox.backend.api_posts.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {
    private String codeCountry;
    private String country;
    private double amount;
    private String currency;
}
