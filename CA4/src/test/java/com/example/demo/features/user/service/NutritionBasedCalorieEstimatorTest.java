package com.example.demo.features.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.features.product.model.NutritionProfile;
import com.example.demo.features.product.model.Product;
import org.junit.jupiter.api.Test;

class NutritionBasedCalorieEstimatorTest {

    private final NutritionBasedCalorieEstimator estimator = new NutritionBasedCalorieEstimator();

    @Test
    void testEstimateCaloriesWithNutritionProfile() {
        Product p = new Product();
        p.setDefaultServingSize(2);
        NutritionProfile profile = new NutritionProfile();
        profile.setCaloriesPerServing(100);
        p.setNutritionProfile(profile);
        double calories = estimator.estimateCalories(p);
        assertEquals(200, calories);
    }

    @Test
    void testEstimateCaloriesWithoutProfile() {
        Product p = new Product();
        p.setDefaultServingSize(1);
        p.setPprice(2.0);
        double calories = estimator.estimateCalories(p);
        assertEquals(100, calories);
    }

    @Test
    void testEstimateCaloriesNullProduct() {
        double calories = estimator.estimateCalories(null);
        assertEquals(0.0, calories);
    }

    @Test
    void testEstimateCaloriesZeroServingSize() {
        Product p = new Product();
        p.setDefaultServingSize(0);
        p.setPprice(1.0);
        double calories = estimator.estimateCalories(p);
        assertEquals(50.0, calories);
    }
}
