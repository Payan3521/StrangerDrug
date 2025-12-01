package com.desarrollox.backend.api_posts.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceDto {
    private String codeCountry;
    private String country;
    private double amount;
    private String currency;
}
