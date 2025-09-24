package com.logistics.shipping_ops_producer.domain.policy;

import com.logistics.shipping_ops_producer.api.dto.ShippingRequestDto;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Component;

//Qué hace?
//Se aplica cuando ya existía un intento previo en Redis.
//Devuelve 2 como intento.
//name() lo identifica.
//Básicamente: “Este request ya pasó una vez, ahora es intento 2”.

@Component
public class SecondTimePolicy implements AttemptPolicy {
    @Override public Single<Integer> resolveAttempt(ShippingRequestDto dto){ return Single.just(2); }
    @Override public String name(){ return "secondTimePolicy"; }
}
