package com.example.demo.features.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("/order_success")
    public String orderSuccess() {
        return "Order_success"; // this should be your Thymeleaf HTML template name
    }

}
