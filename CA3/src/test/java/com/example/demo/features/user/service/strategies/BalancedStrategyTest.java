package com.example.demo.features.user.service.strategies;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.example.demo.features.product.model.NutritionProfile;
import com.example.demo.features.product.model.Product;
import com.example.demo.features.user.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BalancedStrategyTest {

    private CalorieEstimator calorieEstimator;
    private PantryInventory pantryInventory;
    private BalancedStrategy strategy;

    @BeforeEach
    void setUp() {
        calorieEstimator = mock(CalorieEstimator.class);
        pantryInventory = mock(PantryInventory.class);
        strategy = new BalancedStrategy(calorieEstimator, pantryInventory);
    }

    private Product createProduct(int id, String name, double calories, int defaultServing, double price) {
        Product product = mock(Product.class);
        when(product.getPid()).thenReturn(id);
        when(product.getPname()).thenReturn(name);
        when(product.getDefaultServingSize()).thenReturn(defaultServing);
        when(product.getPprice()).thenReturn(price);

        NutritionProfile profile = mock(NutritionProfile.class);
        when(profile.getCaloriesPerServing()).thenReturn(calories);
        when(product.getNutritionProfile()).thenReturn(profile);

        return product;
    }

    @Test
    void testBalancedStrategySelectsClosestCalories() {
        Product p1 = createProduct(1, "Apple", 100, 1, 1.0);
        Product p2 = createProduct(2, "Banana", 150, 1, 1.0);
        Product p3 = createProduct(3, "Chicken", 200, 1, 5.0);

        when(pantryInventory.hasIngredients(any())).thenReturn(true);
        when(calorieEstimator.estimateCalories(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            if (p.getPid() == 1) return 100.0;
            if (p.getPid() == 2) return 150.0;
            return 200.0;
        });

        List<Product> products = Arrays.asList(p1, p2, p3);
        MealPlan plan = strategy.generateMealPlan(300, 2, products);

        List<MealEntry> mondayMeals = plan.getMealsForDay(java.time.DayOfWeek.MONDAY);
        assertEquals(2, mondayMeals.size());
        assertTrue(mondayMeals.stream().anyMatch(m -> m.getProductName().equals("Apple")));
        assertTrue(mondayMeals.stream().anyMatch(m -> m.getProductName().equals("Banana")));
    }
}
