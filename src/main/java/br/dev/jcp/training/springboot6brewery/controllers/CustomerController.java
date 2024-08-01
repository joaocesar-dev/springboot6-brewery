package br.dev.jcp.training.springboot6brewery.controllers;

import br.dev.jcp.training.springboot6brewery.exceptions.NotFoundException;
import br.dev.jcp.training.springboot6brewery.models.CustomerDTO;
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
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v1/customers";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @PostMapping(value = CUSTOMER_PATH)
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customer) {
        CustomerDTO newCustomer = customerService.saveCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customers/" + newCustomer.getId());
        return new ResponseEntity<>(newCustomer, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = CUSTOMER_PATH)
    public List<CustomerDTO> listAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping(value = CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID id){
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }

    @PutMapping(value = CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable UUID customerId, @RequestBody CustomerDTO customer) {
        Optional<CustomerDTO> customerUpdated = customerService.updateCustomer(customerId, customer);
        if (customerUpdated.isPresent()) {
            return new ResponseEntity<>(customerUpdated.get(), HttpStatus.OK);
        }
        throw new NotFoundException();
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> patchCustomer(@PathVariable UUID customerId, @RequestBody CustomerDTO customer) {
        Optional<CustomerDTO> customerUpdated = customerService.patchCustomer(customerId, customer);
        if (customerUpdated.isPresent()) {
            return new ResponseEntity<>(customerUpdated.get(), HttpStatus.OK);
        }
        throw new NotFoundException();
    }

    @DeleteMapping(value = CUSTOMER_PATH_ID)
    public ResponseEntity<String> deleteCustomer(@PathVariable UUID customerId) {
        if (!customerService.deleteCustomer(customerId)) {
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

}
