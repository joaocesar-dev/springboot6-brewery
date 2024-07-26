package br.dev.jcp.training.springboot6brewery.mappers;

import br.dev.jcp.training.springboot6brewery.entities.Beer;
import br.dev.jcp.training.springboot6brewery.models.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    BeerDTO toDTO(Beer beer);
    Beer toEntity(BeerDTO beerDTO);
}
