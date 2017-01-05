package net.caidingke.error;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bowen
 * @create 2017-01-04 16:49
 */
@Getter
@Setter
public class RestError {
    private int errno = 0;
    private String errmsg = "";
}
