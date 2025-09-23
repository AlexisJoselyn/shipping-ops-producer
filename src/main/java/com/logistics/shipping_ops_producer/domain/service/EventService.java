package com.logistics.shipping_ops_producer.domain.service;

import com.logistics.shipping_ops_producer.api.dto.ShippingRequestDto;
import com.logistics.shipping_ops_producer.domain.mapper.EventMapper;
import com.logistics.shipping_ops_producer.domain.port.EventPublisher;
import com.logistics.events.ShippingRequestEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Coordina el flujo de negocio: convierte DTO → Evento Avro
 * y lo publica usando el puerto EventPublisher.
 */

/**
 * Usa el Mapper, aplica Policies y delega en los puertos.
 * Es el “cerebro” que coordina todo, sin saber nada de Kafka o Redis.
 */

@Service
public class EventService {

    private final EventMapper mapper;
    private final EventPublisher<Object> publisher;
    private final String topic;

    public EventService(EventMapper mapper,
                        EventPublisher<Object> publisher,
                        @Value("${kafka.topic}") String topic) {
        this.mapper = mapper;
        this.publisher = publisher;
        this.topic = topic;
    }

    public String process(ShippingRequestDto dto) {
        // convertir el DTO al evento Avro
        ShippingOrderEvent event = mapper.toEvent(dto);

        // publicar a Kafka usando el puerto
        publisher.publish(topic, event.getRequestId(), event);

        return event.getEventId(); // devuelve el ID para rastreo
    }
}
