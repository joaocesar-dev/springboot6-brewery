package br.dev.jcp.training.springboot6brewery.repositories;

import br.dev.jcp.training.springboot6brewery.entities.Beer;
import br.dev.jcp.training.springboot6brewery.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BeerRepository beerRepository;

    Beer testBeer;

    @BeforeEach
    void setUp() {
        testBeer = beerRepository.findAll().getFirst();
    }

    @Rollback
    @Transactional
    @Test
    void shouldCreateACategoryAndAssociateWithBeer() {
        Category savedCategory = categoryRepository.save(Category.builder()
                .description("Ales")
                .build());

        testBeer.addCategory(savedCategory);
        Beer savedBeer = beerRepository.save(testBeer);

        assertThat(savedBeer.getCategories()).hasSize(1);
        assertThat(savedBeer.getCategories()).contains(savedCategory);
    }
}