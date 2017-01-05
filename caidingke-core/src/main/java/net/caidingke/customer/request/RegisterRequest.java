package net.caidingke.customer.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bowen
 * @create 2017-01-04 17:13
 */
@Getter
@Setter
public class RegisterRequest {

    private String username;

    private String password;

    private String telephone;
}
