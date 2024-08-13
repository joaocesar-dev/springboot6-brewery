package br.dev.jcp.training.springboot6brewery.services;

import br.dev.jcp.training.springboot6brewery.models.BeerDTO;
import br.dev.jcp.training.springboot6brewery.models.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, boolean showInventory);
    Optional<BeerDTO> getBeerById(UUID id);
    Optional<BeerDTO> saveBeer(BeerDTO beer);
    Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beer);
    boolean deleteBeer(UUID beerId);
    Optional<BeerDTO> patchBeer(UUID beerId, BeerDTO beer);

}
