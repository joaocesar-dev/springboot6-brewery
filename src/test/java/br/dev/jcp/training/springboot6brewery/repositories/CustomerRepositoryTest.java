package br.dev.jcp.training.springboot6brewery.repositories;

import br.dev.jcp.training.springboot6brewery.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldReturnASavedBeer() {
        Customer savedCustomer = customerRepository.save(Customer.builder().name("Test Customer").build());
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getName()).isEqualTo("Test Customer");
    }
}