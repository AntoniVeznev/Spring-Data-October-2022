import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
public class task_9_IncreaseAgeStoredProcedure {
    public static final String CONNECTION_PATH = "jdbc:mysql://localhost:3306/minions_db";
    public static final String SELECT_RESULT = "select m.name, m.age from minions as m where m.id = ?;";
    public static final String PRINT_FORMAT = "%s %d";

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        int minionID = scanner.nextInt();

        Properties userAndPassword = new Properties();
        userAndPassword.setProperty("user", "root");
        userAndPassword.setProperty("password", "1234");
        Connection connection = DriverManager.getConnection(CONNECTION_PATH, userAndPassword);

        CallableStatement callableStatement = connection.prepareCall("CALL usp_get_older(?)");
        callableStatement.setInt(1, minionID);
        callableStatement.executeUpdate();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RESULT);
        preparedStatement.setInt(1, minionID);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            System.out.printf(PRINT_FORMAT, name, age);
        }
    }
}