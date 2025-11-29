package com.example.demo.features.order.controller;

import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.features.order.model.Orders;
import com.example.demo.features.order.service.OrderServices;
import com.example.demo.features.order.util.Logic;
import com.example.demo.features.product.model.Product;
import com.example.demo.features.product.service.ProductServices;
import com.example.demo.features.user.model.User;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ProductServices productServices;
    @Autowired
    private OrderServices orderServices;

    @PostMapping("/search")
    public String searchProduct(@RequestParam String productName, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        Product product = productServices.getProductByName(productName);
        if (product == null) {
            model.addAttribute("message", "SORRY...! Product Unavailable");
        }
        model.addAttribute("product", product);
        model.addAttribute("orders", orderServices.getOrdersForUser(user));
        return "BuyProduct";
    }

    @PostMapping("/buy")
    public String placeOrder(@ModelAttribute Orders order, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        double total = Logic.countTotal(order.getoPrice(), order.getoQuantity());
        order.setTotalAmmout(total);
        order.setUser(user);
        order.setOrderDate(new Date());

        orderServices.saveOrder(order);
        model.addAttribute("amount", total);
        return "Order_success";
    }

    @GetMapping("/history")
    public String orderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        List<Orders> orders = orderServices.getOrdersForUser(user);
        model.addAttribute("orders", orders);
        return "BuyProduct";
    }
}
