package com.logistics.shipping_ops_producer.domain.mapper;

import com.logistics.shipping_ops_producer.api.dto.ShippingRequestDto;
import com.logistics.events.ShippingOrderEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Convierte un DTO de entrada en un evento Avro listo para publicar en Kafka.
 */
@Component
public class EventMapper {

    public ShippingOrderEvent toEvent(ShippingRequestDto dto) {
        return ShippingOrderEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())   // se genera automáticamente
                .setOrderId(dto.getOrderId())
                .setCustomerId(dto.getCustomerId())
                .setAddress(dto.getAddress())
                .setPriority(dto.getPriority())
                .setCreatedAt(dto.getCreatedAt().toEpochMilli()) // Avro espera long
                .setStatus(dto.getStatus())
                .build();
    }
}
