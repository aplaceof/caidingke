import junit.framework.TestCase;
import net.caidingke.redis.domain.Admin;
import net.caidingke.redis.domain.User;
import net.caidingke.redis.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;

/**
 * @author bowen
 * @create 2016-11-16 18:06
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-redis.xml"})
public class TestRedis extends TestCase implements Serializable {

    @Autowired
    private RedisService service;

    @Test
    public void testRedisIncrement() {
        System.out.println("fanfan"+service.increment("ccccc"));
    }

    @Test
    public void testRedisSet() {
        Admin admin = new Admin("fanfan", 123, 10);
        User user = new User("bowen", 20,admin);
        service.save(user);

    }

    @Test
    public void testRedisGet() {

        if (service.getUserByName("bowen") instanceof User) {
            System.out.println("happy");
        }
    }



}
