package br.dev.jcp.training.springboot6brewery.controllers;


import br.dev.jcp.training.springboot6brewery.entities.Customer;
import br.dev.jcp.training.springboot6brewery.exceptions.NotFoundException;
import br.dev.jcp.training.springboot6brewery.models.CustomerDTO;
import br.dev.jcp.training.springboot6brewery.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldReturnAllCustomers() {
        List<CustomerDTO> customerDTOList = customerController.listAllCustomers();
        assertThat(customerDTOList).hasSize(3);
    }

    @Rollback
    @Transactional
    @Test
    void shouldReturnEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> customerDTOList = customerController.listAllCustomers();
        assertThat(customerDTOList).isEmpty();
    }

    @Test
    void shouldReturnCustomerById() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO customerDTO  = customerController.getCustomerById(customer.getId());
        assertThat(customerDTO).isNotNull();
        assertThat(customerDTO.getId()).isEqualTo(customer.getId());
    }

    @Test
    void shouldReturnNotFouldExcption() {
        assertThatThrownBy(() -> customerController.getCustomerById(UUID.randomUUID()))
                .isInstanceOf(NotFoundException.class);
    }

    @Rollback
    @Transactional
    @Test
    void shouldSaveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("Customer TEST")
                .build();
        ResponseEntity<CustomerDTO> responseEntity = customerController.createCustomer(customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[locationUUID.length - 1]);
        assertThat(savedUUID).isNotNull();

        Customer savedCustomer = customerRepository.findById(savedUUID).orElse(null);
        assertThat(savedCustomer).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void shouldUpdateCustomer() {
        CustomerDTO customerDTO = customerController.listAllCustomers().getFirst();
        UUID customerId = customerDTO.getId();
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName = "Updated Customer TEST";
        customerDTO.setName(customerName);
        ResponseEntity<CustomerDTO> responseEntity = customerController.updateCustomer(customerId, customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Customer customer = customerRepository.findById(customerId).orElse(null);
        assertThat(customer).isNotNull();
        assertThat(customer.getName()).isEqualTo(customerName);
    }

    @Test
    void shouldReturnNotFoundExceptionForUpdate() {
        assertThatThrownBy(() -> customerController.updateCustomer(UUID.randomUUID(), CustomerDTO.builder().build()))
                .isInstanceOf(NotFoundException.class);
    }

    @Rollback
    @Transactional
    @Test
    void shouldDeleteCustomer() {
        Customer customer = customerRepository.findAll().getFirst();
        ResponseEntity<String> responseEntity = customerController.deleteCustomer(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void shouldReturnNotFoundExceptioForDelete() {
        assertThatThrownBy(() -> customerController.deleteCustomer(UUID.randomUUID()))
                .isInstanceOf(NotFoundException.class);
    }
}