package net.caidingke.customer.repository;

import net.caidingke.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author bowen
 * @create 2016-12-30 10:29
 */

public interface CustomerRepository  extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer>{
    Customer findCustomerByUsername(String name);
}
