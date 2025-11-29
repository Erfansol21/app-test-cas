package com.example.demo.features.user.service.strategies;

import com.example.demo.features.product.model.Product;
import com.example.demo.features.user.service.CalorieEstimator;
import com.example.demo.features.user.service.MealEntry;
import com.example.demo.features.user.service.MealPlan;
import com.example.demo.features.user.service.PantryInventory;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class BalancedStrategy implements MealPlanStrategy {

    private final CalorieEstimator calorieEstimator;
    private final PantryInventory pantryInventory;

    public BalancedStrategy(CalorieEstimator calorieEstimator, PantryInventory pantryInventory) {
        this.calorieEstimator = calorieEstimator;
        this.pantryInventory = pantryInventory;
    }

    @Override
    public MealPlan generateMealPlan(double dailyCalorieTarget, int mealsPerDay, List<Product> products) {
        double targetPerMeal = dailyCalorieTarget / mealsPerDay;

        List<ProductScore> scoredProducts = products.stream()
                .filter(Objects::nonNull)
                .map(p -> {
                    double calories = calorieEstimator.estimateCalories(p);
                    double score = Math.abs(targetPerMeal - calories);
                    return new ProductScore(p, calories, score);
                })
                .sorted(Comparator.comparingDouble((ProductScore ps) -> ps.score)
                        .thenComparingInt(ps -> ps.product.getPid()))
                .collect(Collectors.toList());

        return buildWeeklyPlan(scoredProducts, mealsPerDay);
    }

    private MealPlan buildWeeklyPlan(List<ProductScore> scoredProducts, int mealsPerDay) {
        Map<DayOfWeek, List<MealEntry>> plan = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            List<MealEntry> dayMeals = new ArrayList<>();
            for (ProductScore ps : scoredProducts) {
                if (!pantryInventory.hasIngredients(ps.product)) continue;
                pantryInventory.reserve(ps.product);
                dayMeals.add(new MealEntry(ps.product.getPid(), ps.product.getPname(), ps.estimatedCalories));
                if (dayMeals.size() == mealsPerDay) break;
            }
            plan.put(day, dayMeals);
        }
        return new MealPlan(plan);
    }

    private static class ProductScore {
        final Product product;
        final double estimatedCalories;
        final double score;

        ProductScore(Product product, double estimatedCalories, double score) {
            this.product = product;
            this.estimatedCalories = estimatedCalories;
            this.score = score;
        }
    }
}
