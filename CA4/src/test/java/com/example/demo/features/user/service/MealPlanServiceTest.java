package com.example.demo.features.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.DayOfWeek;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.features.product.model.Product;
import com.example.demo.features.product.service.ProductServices;

class MealPlanServiceTest {

    private CalorieEstimator calorieEstimator;
    private PantryInventory pantryInventory;

    @BeforeEach
    void setup() {
        calorieEstimator = mock(CalorieEstimator.class);
        pantryInventory = new SimplePantryInventory();
    }

    // ---------- INPUT VALIDATION ----------

    @Test
    void generateWeeklyPlan_shouldThrow_whenDailyCaloriesIsZeroOrNegative() {
        MealPlanService service = serviceWithProducts(List.of(sampleProduct(1, "A")));

        assertThrows(IllegalArgumentException.class,
                () -> service.generateWeeklyPlan(0, 3));

        assertThrows(IllegalArgumentException.class,
                () -> service.generateWeeklyPlan(-100, 3));
    }

    @Test
    void generateWeeklyPlan_shouldThrow_whenMealsPerDayIsZeroOrNegative() {
        MealPlanService service = serviceWithProducts(List.of(sampleProduct(1, "A")));

        assertThrows(IllegalArgumentException.class,
                () -> service.generateWeeklyPlan(2000, 0));

        assertThrows(IllegalArgumentException.class,
                () -> service.generateWeeklyPlan(2000, -1));
    }

    @Test
    void generateWeeklyPlan_shouldThrow_whenNoProductsAvailable() {
        MealPlanService service = serviceWithProducts(List.of());

        assertThrows(IllegalStateException.class,
                () -> service.generateWeeklyPlan(2000, 3));
    }

    // ---------- HAPPY PATH ----------

    @Test
    void generateWeeklyPlan_shouldCreateMealsForEveryDay() {
        Product chicken = sampleProduct(1, "Chicken");
        Product rice = sampleProduct(2, "Rice");
        Product beef = sampleProduct(3, "Beef");
        Product fish = sampleProduct(4, "Fish");
        Product tofu = sampleProduct(5, "Tofu");
        Product eggs = sampleProduct(6, "Eggs");
        Product beans = sampleProduct(7, "Beans");

        when(calorieEstimator.estimateCalories(any(Product.class)))
                .thenReturn(300.0);

        MealPlanService service =
                serviceWithProducts(List.of(chicken, rice, beef, tofu, fish, eggs, beans));

        MealPlan plan = service.generateWeeklyPlan(1800, 2);

        for (DayOfWeek day : DayOfWeek.values()) {
            assertEquals(2, plan.getMealsForDay(day).size(),
                    "Each day must contain exactly 2 meals");
        }
    }

    // ---------- WEEKLY USAGE LIMIT ----------

    @Test
    void generateWeeklyPlan_shouldFail_whenWeeklyUsageLimitExceeded() {
        Product onlyProduct = sampleProduct(1, "OnlyFood");

        when(calorieEstimator.estimateCalories(onlyProduct))
                .thenReturn(500.0);

        MealPlanService service =
                serviceWithProducts(List.of(onlyProduct));

        assertThrows(IllegalStateException.class,
                () -> service.generateWeeklyPlan(1500, 1),
                "Weekly usage limit (3) should prevent full plan");
    }

    // ---------- INVENTORY EXHAUSTION ----------

    @Test
    void generateWeeklyPlan_shouldFail_whenInventoryRunsOut() {
        Product limited = sampleProduct(1, "Limited");
        limited.setInitialStock(2); // not enough for whole week

        when(calorieEstimator.estimateCalories(limited))
                .thenReturn(400.0);

        MealPlanService service =
                serviceWithProducts(List.of(limited));

        assertThrows(IllegalStateException.class,
                () -> service.generateWeeklyPlan(1200, 1));
    }

    // ---------- SCORING / SORTING ----------

    @Test
    void generateWeeklyPlan_shouldPreferProductCloserToTargetCalories() {
        Product close = sampleProduct(1, "Close");
        Product far = sampleProduct(2, "Far");
        Product beef = sampleProduct(3, "Beef");
        Product fish = sampleProduct(4, "Fish");
        Product tofu = sampleProduct(5, "Tofu");
        Product eggs = sampleProduct(6, "Eggs");
        Product beans = sampleProduct(7, "Beans");

        when(calorieEstimator.estimateCalories(close)).thenReturn(300.0);
        when(calorieEstimator.estimateCalories(far)).thenReturn(900.0);

        MealPlanService service =
                serviceWithProducts(List.of(close, far, beef, tofu, fish, eggs, beans));

        MealPlan plan = service.generateWeeklyPlan(600, 1);

        MealEntry entry =
                plan.getMealsForDay(DayOfWeek.MONDAY).get(0);

        assertEquals("Close", entry.getProductName(),
                "Product with smallest calorie difference must be selected");
    }

    // ---------- HELPERS ----------

    private MealPlanService serviceWithProducts(List<Product> products) {
        ProductServices fakeProductServices =
                new FakeProductServices(products);

        return new MealPlanService(
                fakeProductServices,
                calorieEstimator,
                pantryInventory
        );
    }

    private Product sampleProduct(int id, String name) {
        Product p = new Product();
        p.setPid(id);
        p.setPname(name);
        p.setPprice(10);
        p.setDefaultServingSize(1);
        p.setInitialStock(50);
        return p;
    }

    // ---------- FAKE IMPLEMENTATION ----------

    static class FakeProductServices extends ProductServices {
        private final List<Product> products;

        FakeProductServices(List<Product> products) {
            this.products = products;
        }

        @Override
        public List<Product> getAllProducts() {
            return products;
        }
    }
}
