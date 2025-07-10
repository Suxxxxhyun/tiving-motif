package com.major.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig implements ApplicationListener<ApplicationReadyEvent> {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        reactiveRedisTemplate.opsForValue().get("1") // 스프링부트 애플리케이션 구동시 키값을 조회
                .doOnSuccess(i -> log.info("Initialize to redis connection"))
                .doOnError((err) -> log.info("Failed to Initialize to redis connection: {}", err.getMessage()))
                .subscribe();
    }

    @Bean
    public ReactiveRedisTemplate<String, byte[]> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, byte[]> context = RedisSerializationContext
                .<String, byte[]>newSerializationContext(new StringRedisSerializer())
                .value(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.byteArray()))
                .build();
        return new ReactiveRedisTemplate<>(factory, context);
    }
}
