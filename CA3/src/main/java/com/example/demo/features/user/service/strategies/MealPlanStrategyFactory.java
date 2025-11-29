package com.example.demo.features.user.service.strategies;

import com.example.demo.features.user.service.CalorieEstimator;
import com.example.demo.features.user.service.PantryInventory;
import org.springframework.stereotype.Component;

@Component
public class MealPlanStrategyFactory {

    private final CalorieEstimator calorieEstimator;
    private final PantryInventory pantryInventory;

    public MealPlanStrategyFactory(CalorieEstimator calorieEstimator, PantryInventory pantryInventory) {
        this.calorieEstimator = calorieEstimator;
        this.pantryInventory = pantryInventory;
    }

    public MealPlanStrategy getStrategy(String strategyType) {
        switch (strategyType.toUpperCase()) {
            case "HIGH_PROTEIN":
                return new HighProteinStrategy(calorieEstimator, pantryInventory);
            case "BUDGET_FRIENDLY":
                return new BudgetFriendlyStrategy(calorieEstimator, pantryInventory);
            case "BALANCED":
            default:
                return new BalancedStrategy(calorieEstimator, pantryInventory);
        }
    }
}
