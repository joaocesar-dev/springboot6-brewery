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
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
                .toList();
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
        Optional<Beer> beerEntity = beerRepository.findById(beerId);
        if (beerEntity.isPresent()) {
            beerEntity.get().setBeerName(beer.getBeerName());
            beerEntity.get().setBeerStyle(beer.getBeerStyle());
            beerEntity.get().setUpc(beer.getUpc());
            beerEntity.get().setPrice(beer.getPrice());
            beerRepository.save(beerEntity.get());
        }
       return beerEntity.map(beerMapper::toDTO);
    }

    @Override
    public Optional<BeerDTO> patchBeer(UUID beerId, BeerDTO beer) {
        Optional<Beer> beerEntity = beerRepository.findById(beerId);
        if (beerEntity.isPresent()) {
            if (StringUtils.hasText(beer.getBeerName())) {
                beerEntity.get().setBeerName(beer.getBeerName());
            }
            if (Objects.nonNull(beer.getBeerStyle())) {
                beerEntity.get().setBeerStyle(beer.getBeerStyle());
            }
            if (StringUtils.hasText(beer.getUpc())) {
                beerEntity.get().setUpc(beer.getUpc());
            }
            if (Objects.nonNull(beer.getPrice())) {
                beerEntity.get().setPrice(beer.getPrice());
            }
            if (Objects.nonNull(beer.getQuantityOnHand())) {
                beerEntity.get().setQuantityOnHand(beer.getQuantityOnHand());
            }
            beerRepository.save(beerEntity.get());
        }
        return beerEntity.map(beerMapper::toDTO);
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
