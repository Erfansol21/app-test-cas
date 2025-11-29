package com.example.demo.features.user.service.strategies;

import com.example.demo.features.product.model.Product;
import com.example.demo.features.user.service.CalorieEstimator;
import com.example.demo.features.user.service.MealEntry;
import com.example.demo.features.user.service.MealPlan;
import com.example.demo.features.user.service.PantryInventory;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class BudgetFriendlyStrategy implements MealPlanStrategy {

    private final CalorieEstimator calorieEstimator;
    private final PantryInventory pantryInventory;

    public BudgetFriendlyStrategy(CalorieEstimator calorieEstimator, PantryInventory pantryInventory) {
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
                    double costPerCal = calories > 0 ? p.getPprice() / calories : Double.MAX_VALUE;
                    double calorieScore = Math.abs(targetPerMeal - calories);
                    return new ProductScore(p, calories, costPerCal, calorieScore);
                })
                .sorted(Comparator.comparingDouble((ProductScore ps) -> ps.costPerCal)
                        .thenComparingDouble(ps -> ps.calorieScore)
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
        final double costPerCal;
        final double calorieScore;

        ProductScore(Product product, double estimatedCalories, double costPerCal, double calorieScore) {
            this.product = product;
            this.estimatedCalories = estimatedCalories;
            this.costPerCal = costPerCal;
            this.calorieScore = calorieScore;
        }
    }
}
