package com.logistics.shipping_ops_producer.infrastructure.redis;

import com.logistics.shipping_ops_producer.domain.port.AttemptStateRepository;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.adapter.rxjava.RxJava3Adapter;   // 👈 IMPORTANTE

import java.time.Duration;

//Qué hace?:
//Implementa el puerto AttemptStateRepository.
//Usa ReactiveStringRedisTemplate para interactuar con Redis de manera reactiva.
//Adapta de Project Reactor (Mono) a RxJava (Single) con RxJava3Adapter.
//Métodos
//existsByRequestId → consulta si ya existe un intento para ese requestId (redis.hasKey(...)).
//saveFirstAttempt → guarda un valor "1" para marcar que ese request ya tuvo un primer intento.
//saveEventSnapshot → guarda el evento como JSON con un TTL (expira después de X tiempo).
//Claves Redis:
//shipping:req:{id} → para marcar intentos.
//shipping:event:{id} → para guardar snapshot del evento.
//Resumen: Es el cerebro de control de reintentos, usando Redis como almacenamiento rápido.

@Repository
@RequiredArgsConstructor
public class RedisAttemptStateRepository implements AttemptStateRepository {

    private final ReactiveStringRedisTemplate redis;

    @Override
    public Single<Boolean> existsByRequestId(String requestId) {
        return RxJava3Adapter.monoToSingle(
                redis.hasKey(key(requestId))               // Mono<Boolean>
                        .map(Boolean::booleanValue)
        );
    }

    @Override
    public Single<Boolean> saveFirstAttempt(String requestId) {
        return RxJava3Adapter.monoToSingle(
                redis.opsForValue().set(key(requestId), "1") // Mono<Boolean>
        );
    }

    @Override
    public Single<Boolean> saveEventSnapshot(String requestId, String json, Duration ttl) {
        return RxJava3Adapter.monoToSingle(
                redis.opsForValue().set(snapKey(requestId), json, ttl) // Mono<Boolean>
        );
    }

    private String key(String id)     { return "shipping:req:"   + id; }
    private String snapKey(String id) { return "shipping:event:" + id; }
}
