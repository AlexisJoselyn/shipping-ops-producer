package com.logistics.shipping_ops_producer.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ShippingRequestDto {
    @NotBlank private String orderId;
    @NotBlank private String customerId;
    @NotBlank private String address;
    @NotBlank private String priority;       // HIGH/NORMAL
    @Builder.Default private Instant createdAt = Instant.now();
    @NotBlank private String status;         // REQUESTED, IN_PROGRESS, etc.
}
