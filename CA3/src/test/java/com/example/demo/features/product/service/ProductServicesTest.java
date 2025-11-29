package com.example.demo.features.product.service;

import com.example.demo.features.product.model.Product;
import com.example.demo.features.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServicesTest {

    @Test
    void testAddProduct() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductServices service = new ProductServices();
        ReflectionTestUtils.setField(service, "productRepository", repo);

        Product p = new Product();
        service.addProduct(p);

        verify(repo).save(p);
    }

    @Test
    void testGetAllProducts() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductServices service = new ProductServices();
        ReflectionTestUtils.setField(service, "productRepository", repo);

        when(repo.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));

        var result = service.getAllProducts();

        assertEquals(2, result.size());
        verify(repo).findAll();
    }

    @Test
    void testGetProduct() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductServices service = new ProductServices();
        ReflectionTestUtils.setField(service, "productRepository", repo);

        Product p = new Product();
        p.setPid(5);

        when(repo.findById(5)).thenReturn(Optional.of(p));

        Product result = service.getProduct(5);

        assertEquals(5, result.getPid());
        verify(repo).findById(5);
    }

    @Test
    void testUpdateProduct() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductServices service = new ProductServices();
        ReflectionTestUtils.setField(service, "productRepository", repo);

        Product updated = new Product();
        Product existing = new Product();
        existing.setPid(10);

        when(repo.findById(10)).thenReturn(Optional.of(existing));

        service.updateproduct(updated, 10);

        InOrder order = inOrder(repo);
        order.verify(repo).findById(10);
        order.verify(repo).save(updated);
    }

    @Test
    void testDeleteProduct() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductServices service = new ProductServices();
        ReflectionTestUtils.setField(service, "productRepository", repo);

        service.deleteProduct(7);

        verify(repo).deleteById(7);
    }

    @Test
    void testGetProductByName_ReturnsProduct() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductServices service = new ProductServices();
        ReflectionTestUtils.setField(service, "productRepository", repo);

        Product p = new Product();
        p.setPname("Tea");

        when(repo.findByPname("Tea")).thenReturn(p);

        Product result = service.getProductByName("Tea");

        assertNotNull(result);
        assertEquals("Tea", result.getPname());
    }

    @Test
    void testGetProductByName_ReturnsNull() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductServices service = new ProductServices();
        ReflectionTestUtils.setField(service, "productRepository", repo);

        when(repo.findByPname("Coffee")).thenReturn(null);

        Product result = service.getProductByName("Coffee");

        assertNull(result);
    }
}
