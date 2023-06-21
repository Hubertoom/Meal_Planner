package mealplanner;

import java.util.Scanner;

public class MealPlanner {

    private final Scanner scanner = new Scanner(System.in);
    private String name;
    private Category category;
    private String ingredients;

    public void run() {
        promptMenu();
        getMeal();
        scanner.close();
    }

    private void promptMenu() {
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        category = Category.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Input the meal's name:");
        name = scanner.nextLine();

        System.out.println("Input the ingredients:");
        ingredients = scanner.nextLine();
    }

    private Meal getMeal() {
        Meal meal =  new Meal.MealBuilder()
                .setName(name)
                .setCategory(category)
                .addIngredients(ingredients)
                .build();

        System.out.println(meal);
        System.out.println("The meal has been added!");
        return meal;
    }

}