package mealplanner;

import java.util.*;
import java.util.regex.Pattern;

public class MealPlanner {
    private final List<Meal> mealsList;
    private final Scanner scanner;
    private int meal_id;
    private final DBManagement dbManagement;

    public MealPlanner() {
        this.scanner = new Scanner(System.in);
        this.meal_id = 0;
        this.dbManagement = new DBManagement();
        mealsList = dbManagement.retrieveMealList();
    }

    public void run() {

        while (true) {
            System.out.println("What would you like to do (add, show, exit)?");
            String userRequest = scanner.nextLine();

            switch (userRequest) {
                case "add" -> addMeal();
                case "show" -> show();
                case "exit" -> {
                    scanner.close();
                    dbManagement.closeConnection();
                    System.out.println("Bye!");
                    return;
                }
            }
        }
    }

    private void show() {
        if (mealsList.isEmpty()) {
            System.out.println("No meals saved. Add a meal first.");
        } else {
            mealsList.forEach(System.out::println);
        }
    }

    private void addMeal() {
        Category category = readCategory();
        String name = readMealName();
        List<String> ingredients = readIngredients();

        Meal meal = new Meal.MealBuilder()
                .setCategory(category)
                .setName(name)
                .addIngredientsList(ingredients)
                .setMealId(meal_id)
                .build();

        System.out.println("The meal has been added!");
        meal_id++;

        dbManagement.addMeal(meal);
        dbManagement.addIngredients(meal);

        mealsList.add(meal);
    }

    private Category readCategory() {
        Category category = null;

        boolean isCategoryCorrect = false;
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");

        while (!isCategoryCorrect) {
            String userCategoryChoice = scanner.nextLine();

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