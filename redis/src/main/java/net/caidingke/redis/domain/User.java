package net.caidingke.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author bowen
 * @create 2016-11-18 16:44
 */
@Data
@AllArgsConstructor
public class User implements Serializable{

    private String username;

    private int age;

    private Admin admin;

    public User() {

    }
}
