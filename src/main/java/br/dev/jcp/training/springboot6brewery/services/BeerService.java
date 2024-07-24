package br.dev.jcp.training.springboot6brewery.services;

import br.dev.jcp.training.springboot6brewery.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<Beer> listBeers();
    Optional<Beer> getBeerById(UUID id);
    Beer saveBeer(Beer beer);
    Beer updateBeer(UUID beerId, Beer beer);
    void deleteBeer(UUID beerId);
    void patchBeer(UUID beerId, Beer beer);

}
