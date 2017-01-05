package net.caidingke.profile.security;

import net.caidingke.customer.facade.CustomerFacade;
import net.caidingke.utils.RenderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于处理认证成功的请求,优先级高于default-target-url 之类的配置.
 *
 * @author bowen
 * @create 2016-12-30 12:38
 */

public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private CustomerFacade customerFacade;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath());
        if (authentication != null) {
            RenderUtils.renderJson(httpServletResponse,
                    customerFacade.findCustomerWrapperByUsername(authentication.getName()));
        }

    }
}
