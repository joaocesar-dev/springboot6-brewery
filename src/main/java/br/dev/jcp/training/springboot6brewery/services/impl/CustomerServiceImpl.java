package br.dev.jcp.training.springboot6brewery.services.impl;

import br.dev.jcp.training.springboot6brewery.models.CustomerDTO;
import br.dev.jcp.training.springboot6brewery.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
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
    public Optional<CustomerDTO> getCustomerById(UUID uuid) {
        return Optional.ofNullable(customerMap.get(uuid));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        CustomerDTO customerSaved = CustomerDTO.builder()
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
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
        log.info("Updating customer with id {}", customerId);
        CustomerDTO customerToUpdate = customerMap.get(customerId);

        customerToUpdate.setName(customer.getName());
        customerToUpdate.setUpdateDate(LocalDateTime.now());

        customerMap.put(customerId, customerToUpdate);

        return Optional.of(customerToUpdate);
    }

    @Override
    public Optional<CustomerDTO> patchCustomer(UUID customerId, CustomerDTO customer) {
        log.info("Patching customer with id {}", customerId);
        CustomerDTO customerToUpdate = customerMap.get(customerId);
        if (StringUtils.hasText(customer.getName())) {
            customerToUpdate.setName(customer.getName());
        }
        customerToUpdate.setUpdateDate(LocalDateTime.now());
        return Optional.of(customerToUpdate);
    }

    @Override
    public Boolean deleteCustomer(UUID customerId) {
        log.info("Deleting customer with id {}", customerId);
        customerMap.remove(customerId);
        return true;
    }
}
