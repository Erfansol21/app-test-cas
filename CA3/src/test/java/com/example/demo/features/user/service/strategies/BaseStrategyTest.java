package com.example.demo.features.user.service.strategies;

import static org.mockito.Mockito.*;

import com.example.demo.features.product.model.NutritionProfile;
import com.example.demo.features.product.model.Product;
import com.example.demo.features.user.service.*;

import org.junit.jupiter.api.BeforeEach;

class BaseStrategyTest {

    protected CalorieEstimator calorieEstimator;
    protected PantryInventory pantryInventory;

    @BeforeEach
    void setUp() {
        calorieEstimator = mock(CalorieEstimator.class);
        pantryInventory = mock(PantryInventory.class);
    }

    protected Product createProduct(int id, String name, double calories, double protein, double price, int servings) {
        Product product = mock(Product.class);
        when(product.getPid()).thenReturn(id);
        when(product.getPname()).thenReturn(name);
        when(product.getDefaultServingSize()).thenReturn(servings);
        when(product.getPprice()).thenReturn(price);

        NutritionProfile profile = mock(NutritionProfile.class);
        when(profile.getCaloriesPerServing()).thenReturn(calories);
        when(product.getNutritionProfile()).thenReturn(profile);

        return product;
    }

    protected void mockPantryHasIngredients(Product... products) {
        for (Product p : products) {
            when(pantryInventory.hasIngredients(p)).thenReturn(true);
        }
    }
}
