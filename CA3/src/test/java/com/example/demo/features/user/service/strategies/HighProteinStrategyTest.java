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

class HighProteinStrategyTest {

    private CalorieEstimator calorieEstimator;
    private PantryInventory pantryInventory;
    private HighProteinStrategy strategy;

    @BeforeEach
    void setUp() {
        calorieEstimator = mock(CalorieEstimator.class);
        pantryInventory = mock(PantryInventory.class);
        strategy = new HighProteinStrategy(calorieEstimator, pantryInventory);
    }

    private Product createProduct(int id, String name, double calories, double proteinPerServing, int defaultServing, double price) {
        Product product = mock(Product.class);
        when(product.getPid()).thenReturn(id);
        when(product.getPname()).thenReturn(name);
        when(product.getDefaultServingSize()).thenReturn(defaultServing);
        when(product.getPprice()).thenReturn(price);

        NutritionProfile profile = mock(NutritionProfile.class);
        when(profile.getCaloriesPerServing()).thenReturn(calories);
        when(profile.getProteinGrams()).thenReturn(proteinPerServing);
        when(product.getNutritionProfile()).thenReturn(profile);

        return product;
    }

    @Test
    void testHighProteinStrategyPrioritizesProteinPerCalorie() {
        Product p1 = createProduct(1, "Apple", 100, 2, 1, 1.0);
        Product p2 = createProduct(2, "Chicken", 200, 50, 1, 5.0);
        Product p3 = createProduct(3, "Beans", 150, 15, 1, 2.0);

        when(pantryInventory.hasIngredients(any())).thenReturn(true);
        when(calorieEstimator.estimateCalories(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return p.getNutritionProfile().getCaloriesPerServing();
        });

        List<Product> products = Arrays.asList(p1, p2, p3);
        MealPlan plan = strategy.generateMealPlan(300, 2, products);

        List<MealEntry> mondayMeals = plan.getMealsForDay(java.time.DayOfWeek.MONDAY);
        assertEquals(2, mondayMeals.size());

        assertTrue(mondayMeals.stream().anyMatch(m -> m.getProductName().equals("Chicken")));
    }
}
