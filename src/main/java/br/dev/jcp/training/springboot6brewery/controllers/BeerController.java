package br.dev.jcp.training.springboot6brewery.controllers;

import br.dev.jcp.training.springboot6brewery.exceptions.NotFoundException;
import br.dev.jcp.training.springboot6brewery.models.BeerDTO;
import br.dev.jcp.training.springboot6brewery.models.BeerStyle;
import br.dev.jcp.training.springboot6brewery.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        if (newBeer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String beerId = newBeer.get().getId().toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + "/" + beerId);
        return new ResponseEntity<>(newBeer.orElse(BeerDTO.builder().build()), headers, HttpStatus.CREATED);
    }

    @GetMapping(value = BEER_PATH)
    public Page<BeerDTO> listBeers(@RequestParam(required = false) String beerName,
                                   @RequestParam(required = false) BeerStyle beerStyle,
                                   @RequestParam(required = false) Boolean showInventory,
                                   @RequestParam(required = false) Integer pageNumber,
                                   @RequestParam(required = false) Integer pageSize) {
        boolean showInventoryNormalized = showInventory == null || showInventory;
        return beerService.listBeers(beerName, beerStyle, showInventoryNormalized, pageNumber, pageSize);
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
    public ResponseEntity<BeerDTO> patchBeer(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {
        Optional<BeerDTO> beerUpdated = beerService.patchBeer(beerId, beer);
        if (beerUpdated.isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(beerUpdated.get(), HttpStatus.OK);
    }

    @DeleteMapping(value = BEER_PATH_ID)
    public ResponseEntity<String> deleteBeer(@PathVariable UUID beerId) {
        if (!beerService.deleteBeer(beerId)) {
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }

}
