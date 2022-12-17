import java.sql.*;
import java.util.*;
public class task_7_PrintAllMinionNames {
    public static final String CONNECTION_PATH = "jdbc:mysql://localhost:3306/minions_db";
    private static final String ALL_MINIONS_BY_ID = "SELECT ? FROM minions order by id;";
    private static final String FIRST_HALF_OF_THE_MINIONS = "SELECT m.name from  minions as m where m.id <= ?/2 order by m.id;";
    private static final String SECOND_HALF_OF_THE_MINIONS = "SELECT  m.name from  minions as m where m.id > ?/2 order by m.id desc;";
    private static final String RESULT_FORMAT = "%s%n%s%n";

    public static void main(String[] args) throws SQLException {

        int countOfAllId = 0;
        Queue<String> firstHalf = new LinkedList<>();
        Queue<String> secondHalf = new LinkedList<>();

        Properties usernameAndPassword = new Properties();
        usernameAndPassword.setProperty("user", "root");
        usernameAndPassword.setProperty("password", "1234");
        Connection connection = DriverManager.getConnection(CONNECTION_PATH, usernameAndPassword);

        PreparedStatement allMinions= connection.prepareStatement(ALL_MINIONS_BY_ID);
        PreparedStatement firstHalfOfTheMinions = connection.prepareStatement(FIRST_HALF_OF_THE_MINIONS);
        PreparedStatement secondHalfOfTheMinions = connection.prepareStatement(SECOND_HALF_OF_THE_MINIONS);

        allMinions.setString(1, "id");
        ResultSet minionsSet = allMinions.executeQuery();

        while (minionsSet.next()) {
            countOfAllId++;
        }

        firstHalfOfTheMinions.setInt(1, countOfAllId);
        ResultSet firstHalfSet = firstHalfOfTheMinions.executeQuery();

        for (int i = 0; firstHalfSet.next(); i++) {
            String currentMinionName = firstHalfSet.getString("name");
            firstHalf.add(currentMinionName);
        }

        secondHalfOfTheMinions.setInt(1, countOfAllId);
        ResultSet secondHalfSet = secondHalfOfTheMinions.executeQuery();

        for (int i = 0; secondHalfSet.next(); i++) {
            String currentMinionName = secondHalfSet.getString("name");
            secondHalf.add(currentMinionName);
        }

        for (int i = 0; i < countOfAllId / 2; i++) {
            String currentMinionFromFirstList = firstHalf.remove();
            String currentMinionFromSecondList = secondHalf.remove();
            System.out.printf(RESULT_FORMAT,currentMinionFromFirstList,currentMinionFromSecondList);
        }
    }
}