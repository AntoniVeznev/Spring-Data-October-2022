import java.sql.*;
import java.util.Properties;
public class task_2_GetVillainsNames {

    public static void main(String[] args) throws SQLException {

        Properties userAndPassword = new Properties();
        userAndPassword.setProperty("user", "root");
        userAndPassword.setProperty("password", "1234");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", userAndPassword);

        PreparedStatement statement = connection.prepareStatement(
                "select v.name, count(distinct mv.minion_id) as count from villains as v" +
                        " join minions_villains mv on v.id = mv.villain_id group by v.name" +
                        " having count > ? order by count desc;");

        statement.setInt(1, 15);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String villainName = resultSet.getString("v.name");
            int minionsCount = resultSet.getInt("count");
            System.out.printf("%s %s", villainName, minionsCount);
        }
        connection.close();
    }
}