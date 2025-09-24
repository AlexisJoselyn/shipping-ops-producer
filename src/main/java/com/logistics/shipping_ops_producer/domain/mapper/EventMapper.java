package com.logistics.shipping_ops_producer.domain.mapper;

import com.logistics.events.ShipmentEvent;
import com.logistics.shipping_ops_producer.api.dto.ShippingRequestDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Convierte un DTO de entrada en un evento Avro listo para publicar en Kafka.
 */
@Component
public class EventMapper {

    public ShipmentEvent toEvent(ShippingRequestDto dto) {
        return ShipmentEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())   // se genera automáticamente
                .setShipmentId(dto.getShipmentId())
                .setOrderId(dto.getOrderId())
                .setCustomerId(dto.getCustomerId())
                .setAddress(dto.getAddress())
                .setCity(dto.getCity())
                .setPostalCode(dto.getPostalCode())
                .setServiceLevel(dto.getServiceLevel())
                .setRequestedAt(dto.getRequestedAt()) // Avro espera long
                .setAttemptNumber(dto.getAttemptNumber())
                .setCorrelationId(dto.getCorrelationId())
                .setStatus(dto.getStatus())
                .build();
    }
}

