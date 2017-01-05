package net.caidingke.error;

/**
 * @author bowen
 * @create 2017-01-04 17:23
 */

public class CustomerAlreadyExistsException extends BusinessException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.CustomerAlreadyExists;
    }
}
