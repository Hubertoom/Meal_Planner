package mealplanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Meal {
    private String name;
    private Category category;
    private List<String> ingredients = new ArrayList<>();

    private Meal(String name, Category category, List<String> ingredients) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Category: " + category.name().toLowerCase() +
                "\nName: " + name +
                "\nIngredients: " +
                "\n" + String.join(System.lineSeparator(), ingredients);
    }

    public static class MealBuilder {
        private String name;
        private Category category;
        private List<String> ingredients = new ArrayList<>();

        MealBuilder() {}

        MealBuilder setName(String name) {
            this.name = name;
            return this;
        }

        MealBuilder setCategory(Category category) {
            this.category = category;
            return this;
        }

        MealBuilder addIngredients(List<String> ingredients) {
            this.ingredients.addAll(ingredients);
            return this;
        }

        Meal build() {
            return new Meal(name, category, ingredients);
        }
    }

}