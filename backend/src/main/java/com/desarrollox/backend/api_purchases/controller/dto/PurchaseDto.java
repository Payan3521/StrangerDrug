package com.desarrollox.backend.api_purchases.controller.dto;

import lombok.Data;

@Data
public class PurchaseDto {
    private Long buyerUserId;
    private String videoKey;
    private Long paymentId;
    private double amount;
}
