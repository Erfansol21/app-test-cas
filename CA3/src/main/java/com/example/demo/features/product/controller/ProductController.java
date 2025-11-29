package com.example.demo.features.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.features.product.model.Product;
import com.example.demo.features.product.service.ProductServices;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductServices productServices;

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productServices.addProduct(product);
        return "redirect:/admin/services";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@ModelAttribute Product product, @PathVariable int id) {
        productServices.updateproduct(product, id);
        return "redirect:/admin/services";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productServices.deleteProduct(id);
        return "redirect:/admin/services";
    }
}
