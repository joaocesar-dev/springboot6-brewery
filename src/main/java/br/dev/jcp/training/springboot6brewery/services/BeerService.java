package br.dev.jcp.training.springboot6brewery.services;

import br.dev.jcp.training.springboot6brewery.models.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeers();
    Optional<BeerDTO> getBeerById(UUID id);
    Optional<BeerDTO> saveBeer(BeerDTO beer);
    Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beer);
    Boolean deleteBeer(UUID beerId);
    void patchBeer(UUID beerId, BeerDTO beer);

}
