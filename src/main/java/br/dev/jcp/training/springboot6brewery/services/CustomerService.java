package br.dev.jcp.training.springboot6brewery.services;

import br.dev.jcp.training.springboot6brewery.models.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Optional<CustomerDTO> getCustomerById(UUID uuid);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO saveCustomer(CustomerDTO customer);
    Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer);
    boolean deleteCustomer(UUID customerId);
    Optional<CustomerDTO> patchCustomer(UUID customerId, CustomerDTO customer);
}
