import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class task_6_RemoveVillain {
    public static final String CONNECTION_PATH = "jdbc:mysql://localhost:3306/minions_db";
    public static final String GET_COUNT_OF_MINIONS = "select *  from villains as v join minions_villains mv on v.id = mv.villain_id  where v.id = ?";
    public static final String SELECT_NEEDED_VILLAIN = "select name from villains where id = ?";
    public static final String FREE_THE_MINIONS = "delete mv from minions_villains as mv where mv.villain_id= ?";
    public static final String DELETE_VILLAIN = "delete v from villains as v where v.id = ?";
    public static final String OUTPUT_VILLAIN_DELETED = "%s was deleted%n";
    public static final String OUTPUT_MINIONS_DELETED = "%d minions released";
    public static final String OUTPUT_NO_SUCH_VILLAIN = "No such villain was found";

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        int villainID = Integer.parseInt(scanner.nextLine());
        int countOfAllMinions = 0;
        String nameOfTheVillain = "";

        Properties userAndPassword = new Properties();
        userAndPassword.setProperty("user", "root");
        userAndPassword.setProperty("password", "1234");
        Connection connection = DriverManager.getConnection(CONNECTION_PATH, userAndPassword);

        PreparedStatement preparedStatement = connection.prepareStatement(GET_COUNT_OF_MINIONS);
        preparedStatement.setInt(1, villainID);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            countOfAllMinions++;
        }

        PreparedStatement nameOfVillain = connection.prepareStatement(SELECT_NEEDED_VILLAIN);
        nameOfVillain.setInt(1, villainID);
        ResultSet nameSet = nameOfVillain.executeQuery();

        while (nameSet.next()) {
            nameOfTheVillain = nameSet.getString("name");
        }

        PreparedStatement freeTheMinions = connection.prepareStatement(FREE_THE_MINIONS);
        freeTheMinions.setInt(1, villainID);
        freeTheMinions.executeUpdate();

        PreparedStatement deleteVillains = connection.prepareStatement(DELETE_VILLAIN);
        deleteVillains.setInt(1, villainID);
        deleteVillains.executeUpdate();

        if (!nameOfTheVillain.equals("")) {
            System.out.printf(OUTPUT_VILLAIN_DELETED, nameOfTheVillain);
            System.out.printf(OUTPUT_MINIONS_DELETED, countOfAllMinions);
        } else {
            System.out.println(OUTPUT_NO_SUCH_VILLAIN);
        }
    }
}