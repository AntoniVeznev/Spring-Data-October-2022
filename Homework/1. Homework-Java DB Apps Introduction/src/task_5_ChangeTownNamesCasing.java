import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
public class task_5_ChangeTownNamesCasing {
    public static final String CONNECTION_PATH = "jdbc:mysql://localhost:3306/minions_db";
    public static final String SELECT_TOWNS = "select t.name from towns as t where t.country = ?;";
    public static final String UPDATE_TOWNS_TO_UPPERCASE = "update towns as t set t.name = upper(t.name) where t.country = ?";
    public static final String NEGATIVE_OUTPUT_FORMAT = "No town names were affected.";
    public static final String POSITIVE_OUTPUT_FORMAT = "%d town names were affected.%n";

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        String countryInput = scanner.nextLine();
        List<String> listOfTowns = new ArrayList<>();

        Properties userAndPassword = new Properties();
        userAndPassword.setProperty("user", "root");
        userAndPassword.setProperty("password", "1234");
        Connection connection = DriverManager.getConnection(CONNECTION_PATH, userAndPassword);

        PreparedStatement selectTowns = connection.prepareStatement(SELECT_TOWNS);
        PreparedStatement updateTowns = connection.prepareStatement(UPDATE_TOWNS_TO_UPPERCASE);

        selectTowns.setString(1, countryInput);
        ResultSet allTownsResult = selectTowns.executeQuery();

        if (!allTownsResult.next()) {
            System.out.println(NEGATIVE_OUTPUT_FORMAT);
        } else {
            updateTowns.setString(1, countryInput);
            int totalCountOfTownChanges = updateTowns.executeUpdate();
            updateTowns.executeUpdate();
            selectTowns.setString(1, countryInput);
            ResultSet updatedTowns = selectTowns.executeQuery();

            for (int index = 0; updatedTowns.next(); index++) {
                String currentTown = updatedTowns.getString("name");
                listOfTowns.add(currentTown);
            }
            System.out.printf(POSITIVE_OUTPUT_FORMAT, totalCountOfTownChanges);
            System.out.println(listOfTowns);
        }
    }
}
