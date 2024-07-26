package br.dev.jcp.training.springboot6brewery.controllers;

import br.dev.jcp.training.springboot6brewery.entities.Beer;
import br.dev.jcp.training.springboot6brewery.exceptions.NotFoundException;
import br.dev.jcp.training.springboot6brewery.models.BeerDTO;
import br.dev.jcp.training.springboot6brewery.repositories.BeerRepository;
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
class BeerControllerIT {

    @Autowired
    private BeerController beerController;

    @Autowired
    private BeerRepository beerRepository;

    @Test
    void shouldReturnAllBeers() {
        List<BeerDTO> beerDtoList = beerController.listBeers();
        assertThat(beerDtoList).hasSize(3);
    }

    @Rollback
    @Transactional
    @Test
    void shouldReturnEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO> beerDtoList = beerController.listBeers();
        assertThat(beerDtoList).isEmpty();
    }

    @Test
    void shouldReturnBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDto = beerController.getBeerById(beer.getId());
        assertThat(beerDto).isNotNull();
        assertThat(beerDto.getId()).isEqualTo(beer.getId());
    }

    @Test
    void shouldReturnNotFouldExpetion() {
        assertThatThrownBy(() -> beerController.getBeerById(UUID.randomUUID()))
                .isInstanceOf(NotFoundException.class);
    }

    @Rollback
    @Transactional
    @Test
    void shouldSaveNewBeer() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("Test Beer")
                .build();

        ResponseEntity<BeerDTO> responseEntity = beerController.createBeer(beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[locationUUID.length - 1]);

        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void shouldUpdateBeer() {
        BeerDTO beerDTO = beerController.listBeers().get(0);
        UUID beerId = beerDTO.getId();
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "Test Beer Updated";
        beerDTO.setBeerName(beerName);
        ResponseEntity<BeerDTO> responseEntity = beerController.updateBeer(beerId, beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        Beer beer = beerRepository.findById(beerId).get();
        assertThat(beer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    void shouldReturnNotFoundExceptionForUpdate() {
        assertThatThrownBy(() -> beerController.updateBeer(UUID.randomUUID(), BeerDTO.builder().build()))
                .isInstanceOf(NotFoundException.class);
    }

    @Rollback
    @Transactional
    @Test
    void shouldDeleteBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        ResponseEntity<String> responseEntity = beerController.deleteBeer(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }

    @Test
    void shouldReturnNotFoundExceptioForDeleteById() {
        assertThatThrownBy(() -> beerController.deleteBeer(UUID.randomUUID())).isInstanceOf(NotFoundException.class);
    }
}