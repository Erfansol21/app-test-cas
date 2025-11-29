package com.example.demo.features.user.service;

import com.example.demo.features.product.model.Product;
import com.example.demo.features.product.service.ProductServices;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MealPlanServiceTest {

    static class StubProductServices extends ProductServices {
        private List<Product> products;

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        @Override
        public List<Product> getAllProducts() {
            return products;
        }
    }

    static class StubPantryInventory implements PantryInventory {
        private boolean hasIngredients = true;

        public void setHasIngredients(boolean hasIngredients) {
            this.hasIngredients = hasIngredients;
        }

        @Override
        public boolean hasIngredients(Product product) {
            return hasIngredients;
        }

        @Override
        public void reserve(Product product) {
            // do nothing
        }
    }

    static class StubCalorieEstimator implements CalorieEstimator {
        private double calories = 100;

        public void setCalories(double calories) {
            this.calories = calories;
        }

        @Override
        public double estimateCalories(Product product) {
            return calories;
        }
    }

    @Test
    void testSuccessfulMealPlanCreation() {
        Product apple = new Product();
        apple.setPid(1);
        apple.setPname("Apple");

        Product banana = new Product();
        banana.setPid(2);
        banana.setPname("Banana");

        StubProductServices productServices = new StubProductServices();
        productServices.setProducts(List.of(apple, banana));

        StubPantryInventory pantry = new StubPantryInventory();
        pantry.setHasIngredients(true);

        StubCalorieEstimator estimator = new StubCalorieEstimator();
        estimator.setCalories(100);

        MealPlanService service = new MealPlanService(productServices, estimator, pantry);

        MealPlan plan = service.generateWeeklyPlan(400, 2);

        assertNotNull(plan);
        for (DayOfWeek day : DayOfWeek.values()) {
            assertEquals(2, plan.getMealsForDay(day).size());
        }
    }

    @Test
    void testProductNotFound() {
        StubProductServices productServices = new StubProductServices();
        productServices.setProducts(List.of());

        StubPantryInventory pantry = new StubPantryInventory();
        StubCalorieEstimator estimator = new StubCalorieEstimator();

        MealPlanService service = new MealPlanService(productServices, estimator, pantry);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.generateWeeklyPlan(500, 2));
        assertEquals("No products available for planning", exception.getMessage());
    }

    @Test
    void testInsufficientPantryStock() {
        Product apple = new Product();
        apple.setPid(1);
        apple.setPname("Apple");

        StubProductServices productServices = new StubProductServices();
        productServices.setProducts(List.of(apple));

        StubPantryInventory pantry = new StubPantryInventory();
        pantry.setHasIngredients(false);

        StubCalorieEstimator estimator = new StubCalorieEstimator();

        MealPlanService service = new MealPlanService(productServices, estimator, pantry);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.generateWeeklyPlan(500, 1));
        assertEquals("Unable to fulfill meal plan due to limited inventory", exception.getMessage());
    }

    @Test
    void testInvalidParameters() {
        Product apple = new Product();
        apple.setPid(1);
        apple.setPname("Apple");

        StubProductServices productServices = new StubProductServices();
        productServices.setProducts(List.of(apple));

        StubPantryInventory pantry = new StubPantryInventory();
        StubCalorieEstimator estimator = new StubCalorieEstimator();

        MealPlanService service = new MealPlanService(productServices, estimator, pantry);

        assertThrows(IllegalArgumentException.class, () -> service.generateWeeklyPlan(0, 2));
        assertThrows(IllegalArgumentException.class, () -> service.generateWeeklyPlan(500, 0));
    }

    @Test
    void testNullProductInList() {
        Product apple = new Product();
        apple.setPid(1);
        apple.setPname("Apple");

        StubProductServices productServices = new StubProductServices();

        List<Product> products = new ArrayList<>();
        products.add(apple);
        products.add(null);
        productServices.setProducts(products);

        StubPantryInventory pantry = new StubPantryInventory();
        pantry.setHasIngredients(true);

        StubCalorieEstimator estimator = new StubCalorieEstimator();

        MealPlanService service = new MealPlanService(productServices, estimator, pantry);

        MealPlan plan = service.generateWeeklyPlan(200, 1);

        assertNotNull(plan);
        for (DayOfWeek day : DayOfWeek.values()) {
            assertEquals(1, plan.getMealsForDay(day).size());
        }
    }

    @Test
    void testPartialPantryAvailability() {
        Product apple = new Product();
        apple.setPid(1);
        apple.setPname("Apple");

        Product banana = new Product();
        banana.setPid(2);
        banana.setPname("Banana");

        StubProductServices productServices = new StubProductServices();
        productServices.setProducts(List.of(apple, banana));

        StubPantryInventory pantry = new StubPantryInventory() {
            @Override
            public boolean hasIngredients(Product product) {
                return product.getPid() == 1;
            }
        };

        StubCalorieEstimator estimator = new StubCalorieEstimator();

        MealPlanService service = new MealPlanService(productServices, estimator, pantry);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.generateWeeklyPlan(400, 2));
        assertEquals("Unable to fulfill meal plan due to limited inventory", exception.getMessage());
    }

    @Test
    void testMoreMealsThanProducts() {
        Product apple = new Product();
        apple.setPid(1);
        apple.setPname("Apple");

        StubProductServices productServices = new StubProductServices();
        productServices.setProducts(List.of(apple));

        StubPantryInventory pantry = new StubPantryInventory();
        pantry.setHasIngredients(true);

        StubCalorieEstimator estimator = new StubCalorieEstimator();

        MealPlanService service = new MealPlanService(productServices, estimator, pantry);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.generateWeeklyPlan(500, 3)); // request 3 meals but only 1 product
        assertEquals("Unable to fulfill meal plan due to limited inventory", exception.getMessage());
    }

    @Test
    void testZeroOrNegativeCalorieEstimation() {
        Product apple = new Product();
        apple.setPid(1);
        apple.setPname("Apple");

        StubProductServices productServices = new StubProductServices();
        productServices.setProducts(List.of(apple));

        StubPantryInventory pantry = new StubPantryInventory();
        pantry.setHasIngredients(true);

        StubCalorieEstimator estimator = new StubCalorieEstimator();
        estimator.setCalories(0);

        MealPlanService service = new MealPlanService(productServices, estimator, pantry);

        MealPlan plan = service.generateWeeklyPlan(200, 1);

        assertNotNull(plan);
        for (DayOfWeek day : DayOfWeek.values()) {
            assertEquals(1, plan.getMealsForDay(day).size());
        }
    }
}
