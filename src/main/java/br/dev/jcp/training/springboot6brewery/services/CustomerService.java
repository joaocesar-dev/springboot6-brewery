package br.dev.jcp.training.springboot6brewery.services;

import br.dev.jcp.training.springboot6brewery.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Optional<Customer> getCustomerById(UUID uuid);
    List<Customer> getAllCustomers();
    Customer saveCustomer(Customer customer);
    Customer updateCustomer(UUID customerId, Customer customer);
    void deleteCustomer(UUID customerId);
    void patchCustomer(UUID customerId, Customer customer);
}
