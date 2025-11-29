package com.example.demo.features.user.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.features.user.model.User;
import com.example.demo.features.user.dto.UserLogin;
import com.example.demo.features.user.service.UserServices;
import com.example.demo.features.user.service.UserAuthService;
import com.example.demo.features.order.model.Orders;
import com.example.demo.features.order.service.OrderServices;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private OrderServices orderServices;

    @PostMapping("/login")
    public String login(@ModelAttribute("userLogin") UserLogin login,
                        HttpSession session,
                        Model model) {

        boolean authenticated = userAuthService.authenticate(
                login.getUserEmail(),
                login.getUserPassword()
        );

        if (authenticated) {
            User user = userServices.getUserByEmail(login.getUserEmail());
            session.setAttribute("loggedUser", user);

            List<Orders> orders = orderServices.getOrdersForUser(user);
            model.addAttribute("orders", orders);
            model.addAttribute("name", user.getUname());

            return "BuyProduct";

        } else {
            model.addAttribute("error2", "Invalid email or password");
            return "Login";
        }
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        userServices.addUser(user);
        return "redirect:/admin/services";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute User user, @PathVariable int id) {
        userServices.updateUser(user, id);
        return "redirect:/admin/services";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userServices.deleteUser(id);
        return "redirect:/admin/services";
    }
}
