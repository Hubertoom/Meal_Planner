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
            System.out.println("What would you like to do (add, show, plan, save, exit)?");
            String userRequest = scanner.nextLine();

            switch (userRequest) {
                case "add" -> addMeal();
                case "show" -> show();
                case "plan" -> plan();
                case "save" -> save();
                case "exit" -> {
                    scanner.close();
                    System.out.println("Bye!");
                    return;
                }
            }
        }
    }

    private void save() {
        Map<String, Integer> listOfIngredients = mealDao.getListOfIngredients();
        
    }

    private void plan() {
        for (DaysOfWeek dayOfWeek : DaysOfWeek.values()) {
            addMealForCurrentDay(dayOfWeek.name());
            System.out.println();
        }
        showPlan();
    }

    private void showPlan() {
        for (DaysOfWeek day : DaysOfWeek.values()) {
            List<String> retrievedMeals = new ArrayList<>(mealDao.findAllByDay(day.name()));
            System.out.println(day);
            for (int i = 0; i < retrievedMeals.size() - 2; i++) {
                System.out.println("Breakfast: " + retrievedMeals.get(i));
                System.out.println("Lunch: " + retrievedMeals.get(i + 1));
                System.out.println("Dinner: " + retrievedMeals.get(i + 2));
            }
            System.out.println();
        }
    }
    private void addMealForCurrentDay(String dayOfWeek) {
        System.out.println(dayOfWeek);
        for (Category category : Category.values()) {
            List<Meal> retrievedMeal = new ArrayList<>(mealDao.findAllByCategory(category.name(), "meal"));
            retrievedMeal.forEach(meal -> System.out.println(meal.getName()));
            System.out.printf("Choose the %s for %s from the list above:\n", category.name(), dayOfWeek);

            while (true) {
                String mealName = scanner.nextLine();
                Optional<Meal> meal = retrievedMeal.stream().filter(name -> name.getName().equals(mealName)).findFirst();
                if (meal.isPresent()) {
                    mealDao.addMealToPlan(dayOfWeek, category.name(), meal.get());
                    break;
                }
                System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            }
        }
        System.out.printf("Yeah! We planned the meals for %s.\n", dayOfWeek);
    }

    private void show() {
        List<Meal> retrievedMeals;

        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        String category = scanner.nextLine();
        while (true) {
            if (Pattern.matches("^(breakfast|lunch|dinner)$", category)) {
                retrievedMeals = new ArrayList<>(mealDao.findAllByCategory(category, "meal_id"));
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
        System.out.println();
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