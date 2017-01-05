package net.caidingke.profile.controller;

import net.caidingke.customer.domain.Customer;
import net.caidingke.customer.facade.CustomerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

/**
 * 自定义注解CurrentCustomer给参数赋值.
 *
 * @author bowen
 * @create 2016-12-29 17:05
 */

public class CurrentCustomerHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private CustomerFacade customerFacade;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(CurrentCustomer.class) &&
                methodParameter.getParameterType().equals(Customer.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory)
            throws Exception {
        if (supportsParameter(methodParameter)) {

            Principal principal = nativeWebRequest.getUserPrincipal();
            if (principal != null) {
                return customerFacade.findCustomerByUsername(principal.getName());
            }
        }
        return null;
    }

    public void setCustomerFacade(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }
}
