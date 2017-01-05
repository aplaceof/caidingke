package net.caidingke.profile.security;

import net.caidingke.error.ErrorCode;
import net.caidingke.error.RestError;
import net.caidingke.utils.RenderUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于处理认证失败的AuthenticationFailureHandler,优先级高于authentication-failure-url。
 *
 * @author bowen
 * @create 2016-12-30 12:29
 */

public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException, ServletException {
        final RestError data = new RestError();
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        data.setErrno(ErrorCode.BadCredential.getErrorCode());
        data.setErrmsg(ErrorCode.BadCredential.getErrorMessage());
        RenderUtils.renderJson(httpServletResponse, data);
    }
}
