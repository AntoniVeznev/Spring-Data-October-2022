import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
public class task_3_GetMinionNames {
    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        int villainId = Integer.parseInt(scanner.nextLine());
        String villainName = "";
        int number = 0;
        List<String> minionData = new ArrayList<>();

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement statement = connection.prepareStatement(
                "select m.name, m.age, v.name from villains as v" +
                        " join minions_villains as mv on v.id = mv.villain_id" +
                        " join minions as m on m.id = mv.minion_id where v.id = ?;");


        statement.setInt(1, villainId);
        ResultSet villainResultCheck = statement.executeQuery();
        StringBuilder builder = new StringBuilder();

        if (!villainResultCheck.next()) {
            System.out.printf("No villain with ID %d exists in the database.", villainId);
            connection.close();
            return;
        } else {
            ResultSet minionsInfo = statement.executeQuery();
            while (minionsInfo.next()) {
                number++;
                String vName = minionsInfo.getString("v.name");
                String mName = minionsInfo.getString("m.name");
                String age = minionsInfo.getString("m.age");
                villainName = vName;
                minionData.add(number + "." + " " + mName + " " + age);
            }

            builder.append("Villain: ").append(villainName).append(System.lineSeparator());
            for (String minion : minionData) {
                builder.append(minion).append(System.lineSeparator());
            }
            System.out.print(builder);
        }
        connection.close();
    }
}
