package br.dev.jcp.training.springboot6brewery.repositories;

import br.dev.jcp.training.springboot6brewery.entities.Beer;
import br.dev.jcp.training.springboot6brewery.entities.BeerOrder;
import br.dev.jcp.training.springboot6brewery.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BeerOrderRepositoryTest {

    @Autowired
    private BeerOrderRepository beerOrderRepository;

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    Customer testCustomer;
    Beer testBeer;
    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().getFirst();
        testBeer = beerRepository.findAll().getFirst();
    }

    @Rollback
    @Transactional
    @Test
    void shouldCreateABeerOrder() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("Test new order")
                .customer(testCustomer)
                .build();

        BeerOrder savadeBeerOrder = beerOrderRepository.save(beerOrder);

        assertThat(savadeBeerOrder.getCustomerRef()).isEqualTo("Test new order");

    }
}