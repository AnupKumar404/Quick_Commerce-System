package com.digitalmart.application.Configs;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisConfig {

    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());

//        template.setValueSerializer(new GenericJacksonJsonRedisSerializer());

        template.setHashKeySerializer(new StringRedisSerializer());

//        template.setHashValueSerializer(new GenericJacksonJsonRedisSerializer());

        template.afterPropertiesSet();

        return template;
    }
}
