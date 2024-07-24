package br.dev.jcp.training.springboot6brewery.controllers;

import br.dev.jcp.training.springboot6brewery.exception.NotFoundException;
import br.dev.jcp.training.springboot6brewery.model.Customer;
import br.dev.jcp.training.springboot6brewery.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v1/customers";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @PostMapping(value = CUSTOMER_PATH)
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer newCustomer = customerService.saveCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customers/" + newCustomer.getId());
        return new ResponseEntity<>(newCustomer, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = CUSTOMER_PATH)
    public List<Customer> listAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping(value = CUSTOMER_PATH_ID)
    public Customer getCustomerById(@PathVariable("customerId") UUID id){
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }

    @PutMapping(value = CUSTOMER_PATH_ID)
    public ResponseEntity<Customer> updateCustomer(@PathVariable UUID customerId, @RequestBody Customer customer) {
        Customer customerUpdated = customerService.updateCustomer(customerId, customer);
        return new ResponseEntity<>(customerUpdated, HttpStatus.OK);
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<String> patchCustomer(@PathVariable UUID customerId, @RequestBody Customer customer) {
        customerService.patchCustomer(customerId, customer);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = CUSTOMER_PATH_ID)
    public ResponseEntity<String> deleteCustomer(@PathVariable UUID customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

}
