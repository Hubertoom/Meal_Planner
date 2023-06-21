package mealplanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManagement {
    final String DB_URL = "jdbc:postgresql:meals_db";
    final String USER = "postgres";
    final String PASS = "1111";
    private final Connection connection;

    public DBManagement() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS meals (" +
                    "category VARCHAR(25)," +
                    "meal VARCHAR(30)," +
                    "meal_id INTEGER" +
                    ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ingredients (" +
                    "ingredient VARCHAR(25)," +
                    "ingredient_id INTEGER," +
                    "meal_id INTEGER" +
                    ")");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMeal(Meal meal) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    String.format(
                            "INSERT INTO meals (category, meal, meal_id) " +
                                    "VALUES ('%s', '%s', %d)" +
                                    ";", meal.getCategory(), meal.getName(), meal.getMeal_id())
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addIngredients(Meal meal) {
        try (Statement statement = connection.createStatement()) {
            for (int i = 0; i < meal.getIngredientsList().size(); i++) {
                statement.executeUpdate(
                        String.format(
                                "INSERT INTO ingredients " +
                                        "VALUES ('%s', %d, %d)" +
                                        ";", meal.getIngredientsList().get(i), i, meal.getMeal_id()
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Meal> retrieveMealList() {
        List<Meal> mealList = new ArrayList<>();
        String category;
        String name;
        List<String> ingredientsList = new ArrayList<>();
        int meal_id;

        try (Statement statementForMeals = connection.createStatement()) {
            ResultSet resultSetMeal = statementForMeals.executeQuery(
                    "SELECT * FROM meals;"
            );

            while (resultSetMeal.next()) {
                category = resultSetMeal.getString("category");
                name = resultSetMeal.getString("meal");
                meal_id = resultSetMeal.getInt("meal_id");

                try (Statement statementForIngredients = connection.createStatement()) {
                    ResultSet resultSetIngredients = statementForIngredients.executeQuery(
                            String.format(
                                    "SELECT * FROM ingredients " +
                                            "WHERE meal_id = %d;", meal_id
                            ));
                    while (resultSetIngredients.next()) {
                        ingredientsList.add(resultSetIngredients.getString("ingredient"));
                    }
                }

                mealList.add(
                        new Meal.MealBuilder()
                                .setCategory(Category.valueOf(category))
                                .setName(name)
                                .setMealId(meal_id)
                                .addIngredientsList(ingredientsList)
                                .build()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mealList;
    }

    public void closeConnection() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}