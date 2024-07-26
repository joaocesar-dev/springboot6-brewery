package br.dev.jcp.training.springboot6brewery.repositories;

import br.dev.jcp.training.springboot6brewery.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    private BeerRepository beerRepository;

    @Test
    void shouldReturnASavedBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder().beerName("Beer Test").build());
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getBeerName()).isEqualTo("Beer Test");
        assertThat(savedBeer.getId()).isNotNull();
    }
}