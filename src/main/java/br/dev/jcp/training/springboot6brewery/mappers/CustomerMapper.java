package br.dev.jcp.training.springboot6brewery.mappers;

import br.dev.jcp.training.springboot6brewery.entities.Customer;
import br.dev.jcp.training.springboot6brewery.models.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    CustomerDTO toDTO(Customer customer);
    Customer toEntity(CustomerDTO customerDTO);
}
