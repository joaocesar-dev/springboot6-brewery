package br.dev.jcp.training.springboot6brewery.controllers;

import br.dev.jcp.training.springboot6brewery.exception.NotFoundException;
import br.dev.jcp.training.springboot6brewery.model.Beer;
import br.dev.jcp.training.springboot6brewery.services.BeerService;
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
public class BeerController {
    public static final String BEER_PATH = "/api/v1/beers";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    private final BeerService beerService;

    @PostMapping(value = BEER_PATH)
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) {
        Beer newBeer = beerService.saveBeer(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beers/" + newBeer.getId().toString());
        return new ResponseEntity<>(newBeer, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = BEER_PATH)
    public List<Beer> listBeers(){
        return beerService.listBeers();
    }

    @GetMapping(value = BEER_PATH_ID)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId){
        log.debug("Get beer by id: {}", beerId);
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @PutMapping(value = BEER_PATH_ID)
    public ResponseEntity<Beer> updateBeer(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        Beer beerUpdated = beerService.updateBeer(beerId, beer);
        return new ResponseEntity<>(beerUpdated, HttpStatus.OK);
    }

    @PatchMapping(value = BEER_PATH_ID)
    public ResponseEntity<String> patchBeer(@PathVariable UUID beerId, @RequestBody Beer beer) {
        beerService.patchBeer(beerId, beer);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = BEER_PATH_ID)
    public ResponseEntity<String> deleteBeer(@PathVariable UUID beerId) {
        beerService.deleteBeer(beerId);
        return ResponseEntity.noContent().build();
    }

}
