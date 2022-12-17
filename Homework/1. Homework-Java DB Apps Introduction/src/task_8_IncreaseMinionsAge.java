import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
public class task_8_IncreaseMinionsAge {
    public static final String CONNECTION_PATH = "jdbc:mysql://localhost:3306/minions_db";
    public static final String UPDATE_AGE_OF_NEEDED_MINIONS = "update minions as m set m.age = m.age + 1 where m.id = ?;";
    public static final String UPDATE_NAME_OF_NEEDED_MINIONS = "update minions as m set m.name = LOWER(m.name) where m.id = ?;";
    public static final String SELECT_ALL_MINION_NAMES_AND_AGE = "select m.name, m.age from minions as m;";
    public static final String RESULT_FORMAT = "%s %d%n";

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        String[] minionsID = scanner.nextLine().split(" ");

        Properties userAndPassword = new Properties();
        userAndPassword.setProperty("user", "root");
        userAndPassword.setProperty("password", "1234");
        Connection connection = DriverManager.getConnection(CONNECTION_PATH, userAndPassword);

        PreparedStatement updateAgeStatement = connection.prepareStatement(UPDATE_AGE_OF_NEEDED_MINIONS);
        PreparedStatement updateNameStatement = connection.prepareStatement(UPDATE_NAME_OF_NEEDED_MINIONS);
        PreparedStatement selectResultInfo = connection.prepareStatement(SELECT_ALL_MINION_NAMES_AND_AGE);

        for (int i = 0; i < minionsID.length; i++) {
            updateAgeStatement.setInt(1, Integer.parseInt(minionsID[i]));
            updateAgeStatement.executeUpdate();
            updateNameStatement.setInt(1, Integer.parseInt(minionsID[i]));
            updateNameStatement.executeUpdate();
        }

        ResultSet resultSet = selectResultInfo.executeQuery();
        while (resultSet.next()) {
            String minionName = resultSet.getString("name");
            int minionAge = resultSet.getInt("age");
            System.out.printf(RESULT_FORMAT, minionName, minionAge);
        }
    }
}