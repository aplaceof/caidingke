package net.caidingke.redis.service;

import net.caidingke.redis.domain.User;
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

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    public long increment(String key) {
        System.out.println(key);
        return stringRedisTemplate.opsForValue().increment(key, 1);
    }

    public void save(User user) {

        redisTemplate.opsForValue().set(user.getUsername(), user);
    }

    public Object getUserByName(String key) {

        return redisTemplate.opsForValue().get(key);
    }

}
