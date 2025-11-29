package com.example.demo.features.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.features.admin.dto.AdminLogin;
import com.example.demo.features.admin.model.Admin;
import com.example.demo.features.admin.service.AdminServices;
import com.example.demo.features.admin.service.AdminAuthService;
import com.example.demo.features.product.service.ProductServices;
import com.example.demo.features.user.service.UserServices;
import com.example.demo.features.order.service.OrderServices;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminServices adminServices;

    @Autowired
    private AdminAuthService adminAuthService;

    @Autowired
    private UserServices userServices;

    @Autowired
    private ProductServices productServices;

    @Autowired
    private OrderServices orderServices;

    @PostMapping("/login")
    public String login(@ModelAttribute("adminLogin") AdminLogin login, Model model) {

        boolean authenticated = adminAuthService.authenticate(
                login.getEmail(),
                login.getPassword()
        );

        if (authenticated) {
            return "redirect:/admin/services";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "Login";
        }
    }

    @GetMapping("/services")
    public String dashboard(Model model) {
        model.addAttribute("users", userServices.getAllUser());
        model.addAttribute("admins", adminServices.getAll());
        model.addAttribute("products", productServices.getAllProducts());
        model.addAttribute("orders", orderServices.getOrders());
        return "Admin_Page";
    }

    @GetMapping("/add")
    public String addAdminPage() {
        return "Add_Admin";
    }

    @PostMapping("/add")
    public String addAdmin(@ModelAttribute Admin admin) {
        adminServices.addAdmin(admin);
        return "redirect:/admin/services";
    }

    @GetMapping("/update/{id}")
    public String updateAdminPage(@PathVariable int id, Model model) {
        model.addAttribute("admin", adminServices.getAdmin(id));
        return "Update_Admin";
    }

    @PostMapping("/update/{id}")
    public String updateAdmin(@ModelAttribute Admin admin, @PathVariable int id) {
        adminServices.update(admin, id);
        return "redirect:/admin/services";
    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable int id) {
        adminServices.delete(id);
        return "redirect:/admin/services";
    }
}
