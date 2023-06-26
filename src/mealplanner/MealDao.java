package mealplanner;

import java.util.List;

public interface MealDao {
    List<Meal> findAllByCategory(String category);

    Meal findById(int id);

    void addMeal(Meal meal);

     void addIngredient(String name, int index, int meal_id);
         void update(Meal meal);

    void deleteById(int id);

    int getLastMealId();
}
