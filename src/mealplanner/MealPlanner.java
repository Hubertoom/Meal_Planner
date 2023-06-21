package mealplanner;

import java.util.*;
import java.util.regex.Pattern;

public class MealPlanner {
    private final List<Meal> meals = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        while (true) {
            System.out.println("What would you like to do (add, show, exit)?");
            String userRequest = scanner.nextLine();

            switch (userRequest) {
                case "add" -> addMeal();
                case "show" -> show();
                case "exit" -> {
                    System.out.println("Bye!");
                    scanner.close();
                    return;
                }
            }
        }
    }

    private void show() {
        if (meals.isEmpty()) {
            System.out.println("No meals saved. Add a meal first.");
        } else {
            meals.forEach(System.out::println);
        }
        System.out.println();
    }

    private void addMeal() {
        Category category = readCategory();
        String name = readMealName();
        List<String> ingredients = readIngredients();

        Meal meal = new Meal.MealBuilder()
                .setCategory(category)
                .setName(name)
                .addIngredients(ingredients)
                .build();

        System.out.println("The meal has been added!");
        meals.add(meal);
    }

    private Category readCategory() {
        Category category = null;

        boolean isCategoryCorrect = false;
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");

        while (!isCategoryCorrect) {
            String userCategoryChoice = scanner.nextLine().toUpperCase();

            for (Category mealCategory : Category.values()) {
                if (mealCategory.name().equals(userCategoryChoice)) {
                    isCategoryCorrect = true;
                    category = Category.valueOf(userCategoryChoice);
                    break;
                }
            }

            if (!isCategoryCorrect) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        }
        return category;
    }

    private String readMealName() {
        System.out.println("Input the meal's name:");
        String name = scanner.nextLine();
        while (!Pattern.matches("([a-zA-Z]+\\s*){1,}", name)) {
            System.out.println("Wrong format. Use letters only!");
            name = scanner.nextLine();
        }
        return name;
    }

    private List<String> readIngredients() {
        List<String> ingredients;
        System.out.println("Input the ingredients:");
        String input = scanner.nextLine();

        while (true) {
            ingredients = Arrays.stream(input.split(",")).toList();

            Optional<String> isInputCorrect = ingredients.stream()
                    .filter(element -> !Pattern.matches("(\\s*[a-zA-Z]+\\s*){1,}", element))
                    .findAny();

            if (isInputCorrect.isEmpty()) {
                break;
            }
            System.out.println("Wrong format. Use letters only!");
            input = scanner.nextLine();
        }
        return ingredients;
    }

}