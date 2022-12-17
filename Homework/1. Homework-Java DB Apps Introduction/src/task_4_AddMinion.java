import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
public class task_4_AddMinion {
    public static final String CONNECTION_PATH = "jdbc:mysql://localhost:3306/minions_db";
    public static final String SELECT_TOWN_NAME = "SELECT t.name from towns as t where t.name = ?;";
    public static final String SELECT_VILLAIN_NAME = "SELECT v.name from villains as v where v.name = ?;";
    public static final String GET_TOWN_ID = "SELECT t.id from towns as t where t.name= ?;";
    public static final String GET_MINION_ID = "SELECT m.id from minions as m where m.name = ? limit 1;";
    public static final String GET_VILLAIN_ID = "SELECT v.id from villains as v where v.name = ? limit 1;";
    public static final String INSERT_TOWN = "INSERT INTO towns (towns.name) VALUE (?);";
    public static final String INSERT_VILLAINS = "INSERT INTO villains (villains.name, villains.evilness_factor) VALUE (?, ?);";
    public static final String INSERT_MINIONS = "INSERT INTO minions (minions.name, minions.age, minions.town_id) VALUE (?, ?, ?);";
    public static final String INSERT_MINIONS_VILLAINS = "INSERT into minions_villains set minion_id = ?, villain_id = ?;";

    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        String[] minionDetails = scanner.nextLine().split(" ");
        String minionName = minionDetails[1];
        int minionAge = Integer.parseInt(minionDetails[2]);
        String minionTown = minionDetails[3];

        String[] villainDetails = scanner.nextLine().split(" ");
        String villainName = villainDetails[1];

        int resultMinionID = 0;
        int resultVillainID = 0;

        Properties userAndPassword = new Properties();
        userAndPassword.setProperty("user", "root");
        userAndPassword.setProperty("password", "1234");
        Connection connection = DriverManager.getConnection(CONNECTION_PATH, userAndPassword);

        PreparedStatement checkForTown = connection.prepareStatement(SELECT_TOWN_NAME);
        PreparedStatement checkForVillain = connection.prepareStatement(SELECT_VILLAIN_NAME);
        PreparedStatement getTownId = connection.prepareStatement(GET_TOWN_ID);
        PreparedStatement getMinionId = connection.prepareStatement(GET_MINION_ID);
        PreparedStatement getVillainId = connection.prepareStatement(GET_VILLAIN_ID);
        PreparedStatement insertTowns = connection.prepareStatement(INSERT_TOWN);
        PreparedStatement insertMinions = connection.prepareStatement(INSERT_MINIONS);
        PreparedStatement insertVillains = connection.prepareStatement(INSERT_VILLAINS);
        PreparedStatement insertMinionsAndVillains = connection.prepareStatement(INSERT_MINIONS_VILLAINS);

        checkForTown.setString(1, minionTown);
        ResultSet checkTown = checkForTown.executeQuery();

        if (!checkTown.next()) {
            insertTowns.setString(1, minionTown);
            insertTowns.executeUpdate();
            System.out.printf("Town %s was added to the database.%n", minionTown);
        }

        checkForVillain.setString(1, villainName);
        ResultSet checkVillain = checkForVillain.executeQuery();

        if (!checkVillain.next()) {
            insertVillains.setString(1, villainName);
            insertVillains.setString(2, "evil");
            insertVillains.executeUpdate();
            System.out.printf("Villain %s was added to the database.%n", villainName);
        }

        getTownId.setString(1, minionTown);
        ResultSet townIdResultSet = getTownId.executeQuery();

        if (townIdResultSet.next()) {
            int townId = townIdResultSet.getInt("id");
            insertMinions.setString(1, minionName);
            insertMinions.setInt(2, minionAge);
            insertMinions.setInt(3, townId);
            insertMinions.executeUpdate();
        }

        getMinionId.setString(1, minionName);
        ResultSet minionIdResultSet = getMinionId.executeQuery();

        if (minionIdResultSet.next()) {
            resultMinionID = minionIdResultSet.getInt("id");
        }

        getVillainId.setString(1, villainName);
        ResultSet villainIdResultSet = getVillainId.executeQuery();
        if (villainIdResultSet.next()) {
            resultVillainID = villainIdResultSet.getInt("id");
        }

        insertMinionsAndVillains.setInt(1, resultMinionID);
        insertMinionsAndVillains.setInt(2, resultVillainID);
        insertMinionsAndVillains.executeUpdate();
        System.out.printf("Successfully added %s to be minion of %s.", minionName, villainName);
    }
}