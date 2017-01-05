package net.caidingke.customer.controller;

import lombok.extern.slf4j.Slf4j;
import net.caidingke.customer.facade.CustomerFacade;
import net.caidingke.customer.request.RegisterRequest;
import net.caidingke.customer.wrapper.CustomerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bowen
 * @create 2017-01-04 17:08
 */
@Slf4j
@Controller
@RequestMapping(value = "/api")
public class CustomerController {

    @Autowired
    private CustomerFacade customerFacade;

    @Autowired
    private AuthenticationManager authenticationManager;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CustomerWrapper register(@RequestBody RegisterRequest registerRequest, HttpServletRequest request) {
        CustomerWrapper register = customerFacade.register(registerRequest);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                registerRequest.getUsername(), registerRequest.getPassword());
        try {
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authenticatedUser = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());
        } catch (AuthenticationException e) {
            log.warn("fail to auto login after register", e);
        }
        return register;
    }

}
