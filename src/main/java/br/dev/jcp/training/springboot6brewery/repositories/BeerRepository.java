package br.dev.jcp.training.springboot6brewery.repositories;

import br.dev.jcp.training.springboot6brewery.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
}
