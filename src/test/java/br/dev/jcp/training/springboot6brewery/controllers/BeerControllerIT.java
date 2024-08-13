package br.dev.jcp.training.springboot6brewery.controllers;

import br.dev.jcp.training.springboot6brewery.entities.Beer;
import br.dev.jcp.training.springboot6brewery.exceptions.NotFoundException;
import br.dev.jcp.training.springboot6brewery.models.BeerDTO;
import br.dev.jcp.training.springboot6brewery.models.BeerStyle;
import br.dev.jcp.training.springboot6brewery.repositories.BeerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerIT {

    @Autowired
    private BeerController beerController;

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void shouldReturnAllBeers() {
        List<BeerDTO> beerDtoList = beerController.listBeers(null, null, true);
        assertThat(beerDtoList).hasSize(2413);
    }

    @Test
    void shouldReturnBeersByName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(336)));
    }

    @Test
    void shouldReturnBeersByStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(548)));
    }

    @Test
    void shouldReturnBeersByNameAndStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(310)));
    }

    @Test
    void shouldReturnBeersByNameAndStyleAndShowInventory() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(310)))
                .andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void shouldReturnBeersByNameAndStyleAndNotShowInventory() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(310)))
                .andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Rollback
    @Transactional
    @Test
    void shouldReturnEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO> beerDtoList = beerController.listBeers(null, null, true);
        assertThat(beerDtoList).isEmpty();
    }

    @Test
    void shouldReturnBeerById() {
        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO beerDto = beerController.getBeerById(beer.getId());
        assertThat(beerDto).isNotNull();
        assertThat(beerDto.getId()).isEqualTo(beer.getId());
    }

    @Test
    void shouldReturnNotFoundException() {
        UUID uuid = UUID.randomUUID();
        assertThatThrownBy(() -> beerController.getBeerById(uuid))
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

        Beer beer = beerRepository.findById(savedUUID).orElse(null);
        assertThat(beer).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void shouldUpdateBeer() {
        BeerDTO beerDTO = beerController.listBeers(null, null, true).getFirst();
        UUID beerId = beerDTO.getId();
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "Test Beer Updated";
        beerDTO.setBeerName(beerName);
        ResponseEntity<BeerDTO> responseEntity = beerController.updateBeer(beerId, beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        Beer beer = beerRepository.findById(beerId).orElse(null);
        assertThat(beer).isNotNull();
        assertThat(beer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    void shouldReturnNotFoundExceptionForUpdate() {
        UUID beerId = UUID.randomUUID();
        BeerDTO beerDTO = BeerDTO.builder().build();
        assertThatThrownBy(() -> beerController.updateBeer(beerId, beerDTO))
                .isInstanceOf(NotFoundException.class);
    }

    @Rollback
    @Transactional
    @Test
    void shouldDeleteBeerById() {
        Beer beer = beerRepository.findAll().getFirst();
        ResponseEntity<String> responseEntity = beerController.deleteBeer(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }

    @Test
    void shouldReturnNotFoundExceptionForDeleteById() {
        UUID beerId = UUID.randomUUID();
        assertThatThrownBy(() -> beerController.deleteBeer(beerId)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldReturnBadRequestForBeerWithNameTooLong() throws Exception {
        Beer beerEntity = beerRepository.findAll().getFirst();
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New name 1234567890 9876543210 1478523690 0369852147 XYZ ABC");
        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beerEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest());
    }
}