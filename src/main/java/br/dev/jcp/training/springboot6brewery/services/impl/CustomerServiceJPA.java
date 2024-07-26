package br.dev.jcp.training.springboot6brewery.services.impl;

import br.dev.jcp.training.springboot6brewery.entities.Customer;
import br.dev.jcp.training.springboot6brewery.mappers.CustomerMapper;
import br.dev.jcp.training.springboot6brewery.models.CustomerDTO;
import br.dev.jcp.training.springboot6brewery.repositories.CustomerRepository;
import br.dev.jcp.training.springboot6brewery.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID uuid) {
        return Optional.ofNullable(customerMapper.toDTO(customerRepository.findById(uuid).orElse(null)));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        return customerMapper.toDTO(customerRepository.save(customerMapper.toEntity(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            Customer customerToUpdate = customerOptional.get();
            customerToUpdate.setName(customer.getName());
            customerRepository.save(customerToUpdate);
            return Optional.of(customerMapper.toDTO(customerToUpdate));
        }
        return Optional.empty();
    }

    @Override
    public Optional<CustomerDTO> patchCustomer(UUID customerId, CustomerDTO customer) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent() && StringUtils.hasText(customer.getName())) {
            Customer customerToUpdate = customerOptional.get();
            customerToUpdate.setName(customer.getName());
            customerRepository.save(customerToUpdate);
            return Optional.of(customerMapper.toDTO(customerToUpdate));
        }
        return Optional.empty();
    }

    @Override
    public Boolean deleteCustomer(UUID customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }
}
