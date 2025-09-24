package com.logistics.shipping_ops_producer.infrastructure.kafka;

import com.logistics.shipping_ops_producer.domain.port.EventPublisher;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.reactivex.rxjava3.core.Single;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component("kafkaEventPublisher")
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher<Object> {

    private final KafkaTemplate<String, Object> template;
    private final MeterRegistry meter;

    // métricas globales
    private Counter publishedOk;
    private Counter publishedErr;
    private Timer   publishTimer;

    @PostConstruct
    void initMeters() {
        this.publishedOk  = Counter.builder("shipping_ops_events_published_total")
                .description("Eventos publicados a Kafka (OK)")
                .register(meter);

        this.publishedErr = Counter.builder("shipping_ops_events_published_error_total")
                .description("Errores al publicar eventos a Kafka")
                .register(meter);

        this.publishTimer = Timer.builder("shipping_ops_event_publish_timer")
                .description("Duración de publish() a Kafka")
                .publishPercentileHistogram()
                .register(meter);
    }

    @Override
    public Single<SendResult<String, Object>> publish(String topic, String key, Object value) {
        long startNanos = System.nanoTime();

        // métricas por tópico
        Counter okByTopic = Counter.builder("shipping_ops_events_published_by_topic_total")
                .description("Eventos publicados a Kafka por topic")
                .tag("topic", topic).register(meter);

        Counter errByTopic = Counter.builder("shipping_ops_events_published_by_topic_error_total")
                .description("Errores de publicación por topic")
                .tag("topic", topic).register(meter);

        Timer timerByTopic = Timer.builder("shipping_ops_event_publish_by_topic_timer")
                .description("Duración de publish() por topic")
                .tag("topic", topic).publishPercentileHistogram().register(meter);

        var future = template.send(topic, key, value);

        return Single.create(emitter ->
                future.whenComplete((result, ex) -> {
                    long elapsed = System.nanoTime() - startNanos;
                    publishTimer.record(elapsed, java.util.concurrent.TimeUnit.NANOSECONDS);
                    timerByTopic.record(elapsed, java.util.concurrent.TimeUnit.NANOSECONDS);

                    if (ex != null) {
                        publishedErr.increment();
                        errByTopic.increment();
                        if (!emitter.isDisposed()) emitter.onError(ex);
                    } else {
                        publishedOk.increment();
                        okByTopic.increment();
                        if (!emitter.isDisposed()) emitter.onSuccess(result);
                    }
                })
        );
    }
}
