package com.example.demo.features.site.controller;

import java.util.List;

import com.example.demo.features.admin.service.AdminServices;
import com.example.demo.features.user.dto.UserLogin;
import com.example.demo.features.user.model.User;
import com.example.demo.features.user.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.features.admin.dto.AdminLogin;
import com.example.demo.features.product.model.Product;
import com.example.demo.features.product.service.ProductServices;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController 
{
	@Autowired
	private ProductServices productServices;
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private UserServices userServices;
	@GetMapping(value = {"/home", "/"})
	public String home()
	{
		return "Home";
	}

	@GetMapping("/products")
	public String products( Model model)
	{ 
		List<Product> allProducts = this.productServices.getAllProducts();
		model.addAttribute("products", allProducts);
		return "Products";
	}

	@GetMapping("/location")
	public String location()
	{
		return "Locate_us";
	}

	@GetMapping("/about")
	public String about()
	{
		return "About";
	}

	@GetMapping("/login")
	public String adminLogin(Model model)
	{
		model.addAttribute("adminLogin",new AdminLogin());
        model.addAttribute("userLogin", new UserLogin());
		return "Login";
	}

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userRegistration", new User());
        return "Register";
    }
}
