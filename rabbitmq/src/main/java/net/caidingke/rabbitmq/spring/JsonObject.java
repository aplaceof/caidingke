package net.caidingke.rabbitmq.spring;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.val;

import java.util.ArrayList;

/**
 * 测试rabbitmq jsonMessageConverter
 *
 * @author bowen
 * @create 2016-11-02 16:13
 */

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class JsonObject {

    private String name;

    private int age;

    private boolean sex;
}
