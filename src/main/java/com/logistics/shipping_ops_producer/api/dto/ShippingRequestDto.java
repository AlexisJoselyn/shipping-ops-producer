package com.logistics.shipping_ops_producer.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

@Data
@Builder
public class ShippingRequestDto {
    private String shipmentId;
    private String orderId;
    private String customerId;
    private String address;
    private String city;
    private String postalCode;
    private String serviceLevel;
    private Instant requestedAt;
    private int attemptNumber;
    private String correlationId;
    private String status;
}
