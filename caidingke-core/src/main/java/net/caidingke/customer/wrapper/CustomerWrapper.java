package net.caidingke.customer.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.caidingke.customer.domain.Customer;

import java.util.Date;

/**
 * @author bowen
 * @create 2016-12-30 10:32
 */
@Getter
@Setter
public class CustomerWrapper {

    private Long id;

    private String username;

    private String password;

    private String telephone;

    private boolean enable;

    private Date createTime;

    public static CustomerWrapper transform(Customer customer) {
        CustomerWrapper customerWrapper = new CustomerWrapper();
        customerWrapper.setId(customer.getId());
        customerWrapper.setUsername(customer.getUsername());
        customerWrapper.setPassword(customer.getPassword());
        customerWrapper.setTelephone(customer.getTelephone());
        customerWrapper.setEnable(customer.isEnable());
        customerWrapper.setCreateTime(customer.getCreateTime());
        return customerWrapper;
    }

}
