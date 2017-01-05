package net.caidingke.customer.facade;

import net.caidingke.customer.domain.Customer;
import net.caidingke.customer.request.RegisterRequest;
import net.caidingke.customer.service.CustomerService;
import net.caidingke.customer.wrapper.CustomerWrapper;
import net.caidingke.error.CustomerAlreadyExistsException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author bowen
 * @create 2016-12-30 10:31
 */
@Service
public class CustomerFacade {

    @Autowired
    private CustomerService customerService;

    @Transactional(readOnly = true)
    public Customer findCustomerByUsername(String name) {
        return customerService.findCustomerByUsername(name);
    }

    @Transactional(readOnly = true)
    public CustomerWrapper findCustomerWrapperByUsername(String name) {
        return CustomerWrapper.transform(customerService.findCustomerByUsername(name));
    }

    @Transactional
    public CustomerWrapper register(RegisterRequest registerRequest) {
        checkUsername(registerRequest.getUsername());
        Customer customer = new Customer();
        BeanUtils.copyProperties(registerRequest, customer);
        return CustomerWrapper.transform(customerService.save(customer));
    }

    private void checkUsername(String username) {

        if (customerService.findCustomerByUsername(username) != null) {

            throw new CustomerAlreadyExistsException();
        }
    }
}
