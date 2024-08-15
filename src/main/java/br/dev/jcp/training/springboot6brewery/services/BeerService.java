package br.dev.jcp.training.springboot6brewery.services;

import br.dev.jcp.training.springboot6brewery.models.BeerDTO;
import br.dev.jcp.training.springboot6brewery.models.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, boolean showInventory, Integer pageNumber, Integer pageSize);
    Optional<BeerDTO> getBeerById(UUID id);
    Optional<BeerDTO> saveBeer(BeerDTO beer);
    Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beer);
    boolean deleteBeer(UUID beerId);
    Optional<BeerDTO> patchBeer(UUID beerId, BeerDTO beer);
}
