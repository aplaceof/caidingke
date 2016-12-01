package net.caidingke.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author bowen
 * @create 2016-11-18 17:35
 */
@Data
@AllArgsConstructor
public class Admin {

    private String username;

    private int telephone;

    private int age;

    public Admin() {
    }
}
