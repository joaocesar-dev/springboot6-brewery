package br.dev.jcp.training.springboot6brewery.repositories;

import br.dev.jcp.training.springboot6brewery.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
