package mealplanner;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    private final String name;
    private final Category category;
    private final List<String> ingredientsList;
    private final int meal_id;
    private Meal(String name, Category category, List<String> ingredients, int meal_id) {
        this.name = name;
        this.category = category;
        this.ingredientsList = ingredients;
        this.meal_id = meal_id;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                "\nIngredients: " +
                "\n" + String.join(System.lineSeparator(), ingredientsList) +
                "\n";
    }

    public static class MealBuilder {
        private String name;
        private Category category;
        private final List<String> ingredients = new ArrayList<>();
        private int meal_id;

        MealBuilder() {}

        MealBuilder setName(String name) {
            this.name = name;
            return this;
        }

        MealBuilder setCategory(Category category) {
            this.category = category;
            return this;
        }

        MealBuilder addIngredientsList(List<String> ingredients) {
            this.ingredients.addAll(ingredients);
            return this;
        }

        MealBuilder setMealId(int meal_id) {
            this.meal_id = meal_id;
            return this;
        }

        Meal build() {
            return new Meal(name, category, ingredients, meal_id);
        }
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category.name();
    }

    public List<String> getIngredientsList() {
        return ingredientsList;
    }

    public int getMeal_id() {
        return meal_id;
    }
}