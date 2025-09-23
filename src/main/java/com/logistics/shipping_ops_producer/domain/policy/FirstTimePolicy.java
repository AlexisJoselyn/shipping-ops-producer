package com.logistics.shipping_ops_producer.domain.policy;

import com.logistics.shipping_ops_producer.api.dto.ShippingRequestDto;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Component;

//Qué hace?
//Se aplica cuando es la primera vez que llega un requestId.
//Devuelve el intento 1 como un Single.
//name() sirve para identificarla.
//Básicamente: “Este request es nuevo, asigna intento 1”.

@Component
public class FirstTimePolicy implements AttemptPolicy {
    @Override public Single<Integer> resolveAttempt(ShippingRequestDto dto){ return Single.just(1); }
    @Override public String name(){ return "firstTimePolicy"; }
}
