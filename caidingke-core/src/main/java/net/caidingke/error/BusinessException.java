package net.caidingke.error;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bowen
 * @create 2017-01-04 16:52
 */
@Getter
@Setter
public abstract class BusinessException extends RuntimeException {
    protected final String errMsg = "";
    public abstract ErrorCode getErrorCode();
}
