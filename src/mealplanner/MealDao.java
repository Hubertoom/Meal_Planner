package mealplanner;

import java.util.List;

public interface MealDao {
    List<Meal> findAllByCategory(String category, String orderBy);

    List<String> findAllByDay(String day);
    void addMeal(Meal meal);

     void addIngredient(String name, int index, int meal_id);

    int getLastMealId();

    void addMealToPlan(String day, String category, Meal meal);
}
