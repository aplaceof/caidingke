import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import junit.framework.TestCase;
import net.caidingke.redis.domain.Admin;
import net.caidingke.redis.domain.User;
import net.caidingke.redis.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.util.List;

/**
 * @author bowen
 * @create 2016-11-16 18:06
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-redis.xml"})
public class TestRedis extends TestCase implements Serializable {

    private static final String KEY = "key";

    @Autowired
    private RedisService service;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedisIncrement() {
        System.out.println("fanfan" + service.increment("ccccc"));
    }

    @Test
    public void testRedisSet() {
        Admin admin = new Admin("fanfan", 123, 10);
        User user = new User("bowen", 20, admin);
        service.save(user);

    }

    @Test
    public void testRedisGet() {

        if (service.getUserByName("bowen") instanceof User) {
            System.out.println("happy");
        }
    }

    //http://stackoverflow.com/questions/10750626/transactions-and-watch-statement-in-redis
    //redis transaction
    @Test
    public void testRedisSpringTransaction() {

        List<Object> txResults = stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.watch(KEY);
                operations.multi();
                //operations.opsForSet().add(KEY, "value1");
                operations.opsForValue().increment(KEY, 1);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //This will contain the results of all ops in the transaction
                return operations.exec();
            }
        });
        System.out.println("Number of items added to set: " + txResults.get(0));

    }


}
