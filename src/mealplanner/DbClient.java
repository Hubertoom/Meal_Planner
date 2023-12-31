package mealplanner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbClient {
    private final DataSource dataSource;

    public DbClient(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void run(String query) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Meal> selectAsList(String query) {
        List<Meal> mealsList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()
        ) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                List<String> ingredientsList = new ArrayList<>();
                String name = resultSet.getString("meal");
                int meal_id = resultSet.getInt("meal_id");
                String category = resultSet.getString("category");
                try (Statement statementForIngredients = connection.createStatement()) {
                    ResultSet resultSetIngredients = statementForIngredients.executeQuery(
                            String.format(
                                    "SELECT * FROM ingredients " +
                                            "WHERE meal_id = %d " +
                                            "ORDER BY ingredient_id;", meal_id
                            ));
                    while (resultSetIngredients.next()) {
                        ingredientsList.add(resultSetIngredients.getString("ingredient"));
                    }
                    mealsList.add(
                            new Meal.MealBuilder()
                                    .setCategory(Category.valueOf(category))
                                    .setName(name)
                                    .setMealId(meal_id)
                                    .addIngredientsList(ingredientsList)
                                    .build()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mealsList;
    }

    public int getLastId(String query) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("meal_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public List<String> selectNamesOfMealsAsList(String query) {
        List<String> mealsList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                mealsList.add(resultSet.getString("meal"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mealsList;
    }

    public Map<String, Integer> getListOfIngredients(String query) {
        Map<String, Integer> listOfIngredients = new HashMap<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String ingredientName = resultSet.getString("ingredient");
                int occurrence = resultSet.getInt("occurrences");
                listOfIngredients.put(ingredientName, occurrence);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listOfIngredients;
    }
}
