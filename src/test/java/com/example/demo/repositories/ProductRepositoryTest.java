package com.example.demo.repositories;

import com.example.demo.entities.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("findByPname should return product with given name")
    void testFindByPname() {
        Product product = new Product();
        product.setPname("Cheeseburger");
        product.setPprice(9.99);
        product.setPdescription("A tasty cheeseburger");
        productRepository.save(product);

        Product found = productRepository.findByPname("Cheeseburger");

        assertThat(found).isNotNull();
        assertThat(found.getPname()).isEqualTo("Cheeseburger");
        assertThat(found.getPdescription()).isEqualTo("A tasty cheeseburger");
    }
}
