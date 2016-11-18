package net.caidingke.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * service
 *
 * @author bowen
 * @create 2016-11-16 17:49
 */
@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public long increment(String key) {
        System.out.println(key);
        return stringRedisTemplate.opsForValue().increment(key, 1);
    }

}
