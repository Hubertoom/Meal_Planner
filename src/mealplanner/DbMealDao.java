package mealplanner;

import org.postgresql.ds.PGSimpleDataSource;

import java.util.List;

public class DbMealDao implements MealDao {
    final String DB_URL = "jdbc:postgresql:meals_db";
    final String DB_NAME = "meals_db";
    final String USER = "postgres";
    final String PASS = "1111";

    private final DbClient dbClient;

    private static final String CREATE_MEALS_TABLE = "CREATE TABLE IF NOT EXISTS meals (" +
            "category VARCHAR(25)," +
            "meal VARCHAR(30)," +
            "meal_id INTEGER" +
            ")";
    private static final String CREATE_INGREDIENTS_TABLE = "CREATE TABLE IF NOT EXISTS ingredients (" +
            "ingredient VARCHAR(25)," +
            "ingredient_id INTEGER," +
            "meal_id INTEGER" +
            ")";
    private static final String INSERT_MEAL = "INSERT INTO meals (category, meal, meal_id) " +
            "VALUES ('%s', '%s', %d)" +
            ";";
    private static final String INSERT_INGREDIENT = "INSERT INTO ingredients " +
            "VALUES ('%s', %d, %d)" +
            ";";
    private static final String SELECT_ALL_BY_CATEGORY = "SELECT * FROM meals " +
            "WHERE category = '%s' " +
            "ORDER BY meal_id;";
    private static final String GET_LAST_MEAL_ID = "SELECT MAX(meal_id) AS meal_id FROM meals;";

    public DbMealDao() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName(DB_NAME);
        dataSource.setUser(USER);
        dataSource.setPassword(PASS);

        this.dbClient = new DbClient(dataSource);
        dbClient.run(CREATE_MEALS_TABLE);
        dbClient.run(CREATE_INGREDIENTS_TABLE);
    }


    @Override
    public List<Meal> findAllByCategory(String category) {
        return dbClient.selectAsList(String.format(SELECT_ALL_BY_CATEGORY, category));
    }

    @Override
    public Meal findById(int id) {
        return null;
    }

    @Override
    public void addMeal(Meal meal) {
        dbClient.run(String.format(
                INSERT_MEAL, meal.getCategory(), meal.getName(), meal.getMeal_id()
        ));

        for (int i = 0; i < meal.getIngredientsList().size(); i++) {
            addIngredient(meal.getIngredientsList().get(i), i, meal.getMeal_id());
        }
    }

    @Override
    public void addIngredient(String name, int index, int meal_id) {
        dbClient.run(String.format(
                INSERT_INGREDIENT, name, index, meal_id
        ));
    }

    @Override
    public void update(Meal meal) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public int getLastMealId() {
        return dbClient.getLastId(GET_LAST_MEAL_ID);
    }
}
