package br.dev.jcp.training.springboot6brewery.controllers;

import br.dev.jcp.training.springboot6brewery.model.Beer;
import br.dev.jcp.training.springboot6brewery.model.BeerStyle;
import br.dev.jcp.training.springboot6brewery.services.BeerService;
import br.dev.jcp.training.springboot6brewery.services.impl.BeerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void shouldPatchBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "Changed Name");
        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());
        verify(beerService).patchBeer(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void shouldDeleteBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);
        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(beerService).deleteBeer(uuidArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void shouldUpdateBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isOk());
        verify(beerService).updateBeer(any(UUID.class), any(Beer.class));
    }

    @Test
    void shouldReturnANewBeer() throws Exception {
        Beer newBeer = createInputBeer();
        Beer persistedBeer = becamesInputBeerToPersistedBeer(newBeer);

        given(beerService.saveBeer(any(Beer.class))).willReturn(persistedBeer);

        mockMvc.perform(post(BeerController.BEER_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newBeer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", is(persistedBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(newBeer.getBeerName())));

    }

    @Test
    void shouldReturnABeerById() throws Exception {

        Beer testBeer = createPersistedBeer();

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void shouldReturnNotFoundABeerById() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllBeers() throws Exception {
        given(beerService.listBeers()).willReturn(createPersistedBeerList());

        mockMvc.perform(get(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    private Beer createPersistedBeer() {
         return Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Test Beer")
                .beerStyle(BeerStyle.LAGER)
                .upc("12346789")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(100)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    private Beer createInputBeer() {
        return Beer.builder()
                .beerName("Test New Beer")
                .beerStyle(BeerStyle.PILSNER)
                .upc("121212121")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(110)
                .build();
    }

    private Beer becamesInputBeerToPersistedBeer(Beer inputBeer) {
        return Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName(inputBeer.getBeerName())
                .beerStyle(inputBeer.getBeerStyle())
                .upc(inputBeer.getUpc())
                .price(inputBeer.getPrice())
                .quantityOnHand(inputBeer.getQuantityOnHand())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    private List<Beer> createPersistedBeerList() {
        List<Beer> beerList = new ArrayList<>();
        Beer testBeer = createPersistedBeer();
        beerList.add(testBeer);
        beerList.add(Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Test Beer 2")
                .beerStyle(BeerStyle.WHEAT)
                .upc("0012346789")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(90)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());
        beerList.add(Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Test Beer 3")
                .beerStyle(BeerStyle.STOUT)
                .upc("0012346789")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(80)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build()
        );
        return beerList;
    }

}