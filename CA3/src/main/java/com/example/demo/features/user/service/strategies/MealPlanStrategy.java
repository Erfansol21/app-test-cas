package com.example.demo.features.user.service.strategies;

import com.example.demo.features.product.model.Product;
import com.example.demo.features.user.service.MealPlan;

import java.util.List;

public interface MealPlanStrategy {
    MealPlan generateMealPlan(double dailyCalorieTarget, int mealsPerDay, List<Product> products);
}
