package br.dev.jcp.training.springboot6brewery.services.impl;

import br.dev.jcp.training.springboot6brewery.entities.Beer;
import br.dev.jcp.training.springboot6brewery.mappers.BeerMapper;
import br.dev.jcp.training.springboot6brewery.models.BeerDTO;
import br.dev.jcp.training.springboot6brewery.models.BeerStyle;
import br.dev.jcp.training.springboot6brewery.repositories.BeerRepository;
import br.dev.jcp.training.springboot6brewery.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private static final String LIKE_EXPRESSION = "%%%s%%";
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int DEFAULT_PAGE_OFFSET = 0;

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, boolean showInventory, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Beer> beerPage;
        if (StringUtils.hasText(beerName) && Objects.isNull(beerStyle)) {
            beerPage = listBeersByName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && Objects.nonNull(beerStyle)) {
            beerPage = listBeersByStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && Objects.nonNull(beerStyle)) {
            beerPage = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }
        if (!showInventory) {
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }
        return beerPage.map(beerMapper::toDTO);
    }

    PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE_OFFSET;
        }
        if (pageSize == null ) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }
        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Page<Beer> listBeersByName(String beerName, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase(String.format(LIKE_EXPRESSION, beerName), pageable);
    }

    private Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageable);
    }

    private Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String.format(LIKE_EXPRESSION, beerName), beerStyle, pageable);
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
    public boolean deleteBeer(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

}
