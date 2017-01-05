package net.caidingke.profile.service;

import lombok.Setter;
import net.caidingke.customer.domain.Customer;
import net.caidingke.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bowen
 * @create 2016-12-30 12:12
 */

public class CustomerUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    @Setter
    private CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerService.findCustomerByUsername(username);
        if (customer == null) {
            throw new UsernameNotFoundException("The customer was not found");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new
                SimpleGrantedAuthority("ROLE_USER"));
        return new User(username, customer.getPassword(), true, true, true, true, grantedAuthorities);
    }
}
