import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnector {
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/countriesData";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "s1234taE!5678";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM countries";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String countryName = resultSet.getString("displayName");
                int population = resultSet.getInt("population");
                System.out.println("Country: " + countryName + ", Population: " + population);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
