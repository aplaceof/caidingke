package net.caidingke.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author bowen
 * @create 2017-01-04 16:52
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    BadCredential(10001, "用户名或密码错误"),
    CustomerAlreadyExists(10002, "用户已存在");

    private int errorCode;

    private String errorMessage;


}
