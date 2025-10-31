package com.example.demo.controllers;

import com.example.demo.entities.Product;
import com.example.demo.services.ProductServices;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServices productServices;

    @Test
    void testAddProductRedirects() throws Exception {
        mockMvc.perform(post("/addingProduct")
                .param("pname", "Test")
                .param("pprice", "9.99")
                .param("pdescription", "desc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
        Mockito.verify(productServices).addProduct(Mockito.any(Product.class));
    }

    @Test
    void testUpdateProductRedirects() throws Exception {
        mockMvc.perform(get("/updatingProduct/1")
                .param("pname", "Updated")
                .param("pprice", "19.99")
                .param("pdescription", "updated"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
        Mockito.verify(productServices).updateproduct(Mockito.any(Product.class), Mockito.eq(1));
    }

    @Test
    void testDeleteProductRedirects() throws Exception {
        mockMvc.perform(get("/deleteProduct/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/services"));
        Mockito.verify(productServices).deleteProduct(1);
    }
}
