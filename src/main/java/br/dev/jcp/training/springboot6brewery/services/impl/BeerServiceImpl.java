package br.dev.jcp.training.springboot6brewery.services.impl;

import br.dev.jcp.training.springboot6brewery.model.Beer;
import br.dev.jcp.training.springboot6brewery.model.BeerStyle;
import br.dev.jcp.training.springboot6brewery.services.BeerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12346")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }


    @Override
    public List<Beer> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {
        return beerMap.get(id);
    }

    @Override
    public Beer saveBeer(Beer beer) {
        Beer savedBeer = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public Beer updateBeer(UUID beerId, Beer beer) {
        log.info("Updating beer with id: {}", beerId);
        Beer beerToUpdate = beerMap.get(beerId);

        beerToUpdate.setBeerName(beer.getBeerName());
        beerToUpdate.setBeerStyle(beer.getBeerStyle());
        beerToUpdate.setPrice(beer.getPrice());
        beerToUpdate.setUpc(beer.getUpc());
        beerToUpdate.setQuantityOnHand(beer.getQuantityOnHand());

        beerToUpdate.setUpdateDate(LocalDateTime.now());

        beerMap.put(beerId, beerToUpdate);

        return beerToUpdate;
    }

    @Override
    public void patchBeer(UUID beerId, Beer beer) {
        log.info("Patching beer with id: {}", beerId);
        Beer beerPatched = beerMap.get(beerId);
        if (StringUtils.hasText(beer.getBeerName())) {
            beerPatched.setBeerName(beer.getBeerName());
        }
        if (Objects.nonNull(beer.getBeerStyle())) {
            beerPatched.setBeerStyle(beer.getBeerStyle());
        }
        if (Objects.nonNull(beer.getPrice())) {
            beerPatched.setPrice(beer.getPrice());
        }
        if (StringUtils.hasText(beer.getUpc())) {
            beerPatched.setUpc(beer.getUpc());
        }
        if (Objects.nonNull(beer.getQuantityOnHand())) {
            beerPatched.setQuantityOnHand(beer.getQuantityOnHand());
        }
        beerPatched.setUpdateDate(LocalDateTime.now());
    }

    @Override
    public void deleteBeer(UUID beerId) {
        log.info("Deleting beer with id {}", beerId);
        beerMap.remove(beerId);
    }

}
