package br.dev.jcp.training.springboot6brewery.controllers;

import br.dev.jcp.training.springboot6brewery.exceptions.NotFoundException;
import br.dev.jcp.training.springboot6brewery.models.BeerDTO;
import br.dev.jcp.training.springboot6brewery.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
public class BeerController {
    public static final String BEER_PATH = "/api/v1/beers";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    private final BeerService beerService;

    @PostMapping(value = BEER_PATH)
    public ResponseEntity<BeerDTO> createBeer(@RequestBody BeerDTO beer) {
        Optional<BeerDTO> newBeer = beerService.saveBeer(beer);
        String beerId = newBeer.orElse(BeerDTO.builder().build()).getId().toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + "/" + beerId);
        return new ResponseEntity<>(newBeer.orElse(BeerDTO.builder().build()), headers, HttpStatus.CREATED);
    }

    @GetMapping(value = BEER_PATH)
    public List<BeerDTO> listBeers(){
        return beerService.listBeers();
    }

    @GetMapping(value = BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId){
        log.debug("Get beer by id: {}", beerId);
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @PutMapping(value = BEER_PATH_ID)
    public ResponseEntity<BeerDTO> updateBeer(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDTO beer) {
        Optional<BeerDTO> beerUpdated = beerService.updateBeer(beerId, beer);
        if (beerUpdated.isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(beerUpdated.get(), HttpStatus.OK);
    }

    @PatchMapping(value = BEER_PATH_ID)
    public ResponseEntity<String> patchBeer(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {
        Optional<BeerDTO> beerUpdated = beerService.patchBeer(beerId, beer);
        if (beerUpdated.isEmpty()) {
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = BEER_PATH_ID)
    public ResponseEntity<String> deleteBeer(@PathVariable UUID beerId) {
        if (!beerService.deleteBeer(beerId)) {
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

}
