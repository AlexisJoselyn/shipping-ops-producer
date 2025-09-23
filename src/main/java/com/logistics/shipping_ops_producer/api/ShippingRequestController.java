package com.logistics.shipping_ops_producer.api;

import com.logistics.shipping_ops_producer.api.dto.ShippingRequestDto;
import com.logistics.shipping_ops_producer.domain.service.EventService;
import io.reactivex.rxjava3.core.Single;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipping-requests")
// Último paso: expone el endpoint REST.
// Solo recibe DTO, lo valida y delega al EventService.
public class ShippingRequestController {

    private final EventService service;

    public ShippingRequestController(EventService service) {
        this.service = service;
    }

    @Operation(summary = "Solicita envío y publica evento Avro en Kafka")
    @PostMapping
    public Single<ResponseEntity<String>> create(@Valid @RequestBody ShippingRequestDto dto) {
        return service.process(dto)
                .map(id -> ResponseEntity.accepted().body(id));
    }
}
