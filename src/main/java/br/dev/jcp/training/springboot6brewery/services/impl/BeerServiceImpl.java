package br.dev.jcp.training.springboot6brewery.services.impl;

import br.dev.jcp.training.springboot6brewery.models.BeerDTO;
import br.dev.jcp.training.springboot6brewery.models.BeerStyle;
import br.dev.jcp.training.springboot6brewery.services.BeerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO beer1 = BeerDTO.builder()
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

        BeerDTO beer2 = BeerDTO.builder()
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

        BeerDTO beer3 = BeerDTO.builder()
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
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, boolean showInventory, Integer pageNumber, Integer pageSize) {
        ArrayList<BeerDTO> beers;
        if (StringUtils.hasText(beerName) && Objects.isNull(beerStyle)) {
            beers = (ArrayList<BeerDTO>) getBeersByName(beerName);
        } else if (!StringUtils.hasText(beerName) && Objects.nonNull(beerStyle)) {
            beers = (ArrayList<BeerDTO>) getBeersByStyle(beerStyle);
        } else if (StringUtils.hasText(beerName) && Objects.nonNull(beerStyle)){
            beers = (ArrayList<BeerDTO>) getBeersByNameAndStyle(beerName, beerStyle);
        } else {
            beers = new ArrayList<>(beerMap.values());
        }
        if (Boolean.FALSE.equals(showInventory)) {
            beers.forEach(beer -> beer.setQuantityOnHand(null));
        }
        return new PageImpl<>(beers);
    }

    private List<BeerDTO> getBeersByName(String beerName) {
        ArrayList<BeerDTO> beers = new ArrayList<>(beerMap.values());
        return beers.stream().filter(beerDTO -> beerDTO.getBeerName().contains(beerName)).toList();
    }

    private List<BeerDTO> getBeersByStyle(BeerStyle beerStyle) {
        ArrayList<BeerDTO> beers = new ArrayList<>(beerMap.values());
        return beers.stream().filter(beerDTO -> beerDTO.getBeerStyle() == beerStyle).toList();
    }

    private List<BeerDTO> getBeersByNameAndStyle(String beerName, BeerStyle beerStyle) {
        ArrayList<BeerDTO> beers = new ArrayList<>(beerMap.values());
        return beers.stream().filter(beerDTO -> beerDTO.getBeerName().contains(beerName) && beerDTO.getBeerStyle() == beerStyle).toList();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMap.get(id));
    }

    @Override
    public Optional<BeerDTO> saveBeer(BeerDTO beer) {
        if (!isValid(beer)) {
            return Optional.empty();
        }

        BeerDTO savedBeer = BeerDTO.builder()
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

        return Optional.of(savedBeer);
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beer) {
        log.info("Updating beer with id: {}", beerId);
        BeerDTO beerToUpdate = beerMap.get(beerId);

        beerToUpdate.setBeerName(beer.getBeerName());
        beerToUpdate.setBeerStyle(beer.getBeerStyle());
        beerToUpdate.setPrice(beer.getPrice());
        beerToUpdate.setUpc(beer.getUpc());
        beerToUpdate.setQuantityOnHand(beer.getQuantityOnHand());
        beerToUpdate.setUpdateDate(LocalDateTime.now());

        beerMap.put(beerId, beerToUpdate);

        return Optional.of(beerToUpdate);
    }

    @Override
    public Optional<BeerDTO> patchBeer(UUID beerId, BeerDTO beer) {
        log.info("Patching beer with id: {}", beerId);
        BeerDTO beerPatched = beerMap.get(beerId);
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
        return Optional.of(beerPatched);
    }

    @Override
    public boolean deleteBeer(UUID beerId) {
        log.info("Deleting beer with id {}", beerId);
        beerMap.remove(beerId);
        return true;
    }

    private boolean isValid(BeerDTO beer) {
        return StringUtils.hasText(beer.getBeerName())
                && Objects.nonNull(beer.getBeerStyle())
                && StringUtils.hasText(beer.getUpc())
                && Objects.nonNull(beer.getPrice());
    }
}
