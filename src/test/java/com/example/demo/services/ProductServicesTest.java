package com.example.demo.services;

import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductServicesTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServices productServices;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setPid(1);
        product.setPname("Burger");
        product.setPprice(10.5);
        product.setPdescription("Delicious burger");
    }

    @Test
    @DisplayName("addProduct should call save on repository")
    void testAddProduct() {
        productServices.addProduct(product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("getAllProducts should return all products")
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        List<Product> result = productServices.getAllProducts();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPname()).isEqualTo("Burger");
    }

    @Test
    @DisplayName("getProduct should return product by id")
    void testGetProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        Product result = productServices.getProduct(1);
        assertThat(result).isNotNull();
        assertThat(result.getPname()).isEqualTo("Burger");
    }

    @Test
    @DisplayName("updateproduct should save when id matches existing product")
    void testUpdateProduct() {
        Product updated = new Product();
        updated.setPname("Pizza");
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        productServices.updateproduct(updated, 1);
        assertThat(updated.getPid()).isEqualTo(1);
        verify(productRepository, times(1)).save(updated);
    }

    @Test
    @DisplayName("deleteProduct should call deleteById on repository")
    void testDeleteProduct() {
        productServices.deleteProduct(1);
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("getProductByName should return product when found")
    void testGetProductByNameFound() {
        when(productRepository.findByPname("Burger")).thenReturn(product);
        Product result = productServices.getProductByName("Burger");
        assertThat(result).isNotNull();
        assertThat(result.getPname()).isEqualTo("Burger");
    }

    @Test
    @DisplayName("getProductByName should return null when not found")
    void testGetProductByNameNotFound() {
        when(productRepository.findByPname("Pizza")).thenReturn(null);
        Product result = productServices.getProductByName("Pizza");
        assertThat(result).isNull();
    }
}
