package net.caidingke.customer.service;

import net.caidingke.customer.domain.Customer;
import net.caidingke.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author bowen
 * @create 2016-12-30 10:30
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    public Customer findCustomerByUsername(String name) {
        return customerRepository.findCustomerByUsername(name);
    }

    public Customer save(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setCreateTime(new Date());
        return customerRepository.save(customer);
    }
}
