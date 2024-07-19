package br.dev.jcp.training.springboot6brewery.services.impl;

import br.dev.jcp.training.springboot6brewery.model.Customer;
import br.dev.jcp.training.springboot6brewery.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 3")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap = new HashMap<>();
        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public Customer getCustomerById(UUID uuid) {
        return customerMap.get(uuid);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        Customer customerSaved = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .name(customer.getName())
                .build();
        customerMap.put(customer.getId(), customerSaved);
        return customerSaved;
    }

    @Override
    public Customer updateCustomer(UUID customerId, Customer customer) {
        log.info("Updating customer with id {}", customerId);
        Customer customerToUpdate = customerMap.get(customerId);

        customerToUpdate.setName(customer.getName());
        customerToUpdate.setUpdateDate(LocalDateTime.now());

        customerMap.put(customerId, customerToUpdate);

        return customerToUpdate;
    }

    @Override
    public void patchCustomer(UUID customerId, Customer customer) {
        log.info("Patching customer with id {}", customerId);
        Customer customerToUpdate = customerMap.get(customerId);
        if (StringUtils.hasText(customer.getName())) {
            customerToUpdate.setName(customer.getName());
        }
        customerToUpdate.setUpdateDate(LocalDateTime.now());
    }

    @Override
    public void deleteCustomer(UUID customerId) {
        log.info("Deleting customer with id {}", customerId);
        customerMap.remove(customerId);
    }
}
