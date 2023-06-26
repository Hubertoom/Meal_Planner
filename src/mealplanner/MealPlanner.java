package mealplanner;

import java.util.*;
import java.util.regex.Pattern;

public class MealPlanner {
    private final Scanner scanner;
    private final MealDao mealDao;

    public MealPlanner() {
        this.scanner = new Scanner(System.in);
        this.mealDao = new DbMealDao();
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
                   // mealDao.closeConnection();
                    System.out.println("Bye!");
                    return;
                }
            }
        }
    }

    private void show() {
        List<Meal> retrievedMeals;

        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        String category = scanner.nextLine();
        while (true) {
            if (Pattern.matches("^(breakfast|lunch|dinner)$", category)) {
                retrievedMeals = new ArrayList<>(mealDao.findAllByCategory(category));
                break;
            } else {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                category = scanner.nextLine();
            }

        }
        if (retrievedMeals.isEmpty()) {
            System.out.println("No meals found.");
        } else {
            System.out.printf("Category: %s\n\n", category);
            retrievedMeals.forEach(System.out::println);
        }
}

    private void addMeal() {
        int meal_id = mealDao.getLastMealId() + 1;
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

        mealDao.addMeal(meal);
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
        while (!Pattern.matches("([a-zA-Z]+\\s*)+", name)) {
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
                    .filter(element -> !Pattern.matches("(\\s*[a-zA-Z]+\\s*)+", element))
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