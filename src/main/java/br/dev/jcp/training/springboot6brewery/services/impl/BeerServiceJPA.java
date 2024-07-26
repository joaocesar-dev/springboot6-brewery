package br.dev.jcp.training.springboot6brewery.services.impl;

import br.dev.jcp.training.springboot6brewery.entities.Beer;
import br.dev.jcp.training.springboot6brewery.mappers.BeerMapper;
import br.dev.jcp.training.springboot6brewery.models.BeerDTO;
import br.dev.jcp.training.springboot6brewery.repositories.BeerRepository;
import br.dev.jcp.training.springboot6brewery.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.toDTO(beerRepository.findById(id).orElse(null)));
    }

    @Override
    public Optional<BeerDTO> saveBeer(BeerDTO beerDTO) {
        return Optional.of(beerMapper.toDTO(beerRepository.save(beerMapper.toEntity(beerDTO))));
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beer) {
        Beer beerEntity = beerRepository.findById(beerId).orElse(null);
        if (Objects.nonNull(beerEntity)) {
            beerEntity.setBeerName(beer.getBeerName());
            beerEntity.setBeerStyle(beer.getBeerStyle());
            beerEntity.setUpc(beer.getUpc());
            beerEntity.setPrice(beer.getPrice());
            beerRepository.save(beerEntity);
        }
       return Objects.nonNull(beerEntity) ? Optional.of(beerMapper.toDTO(beerEntity)) : Optional.empty();
    }

    @Override
    public void patchBeer(UUID beerId, BeerDTO beer) {
    }

    @Override
    public Boolean deleteBeer(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

}
