import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TemperatureChangeTimeline extends Application{
	@Override
    public void start(Stage stage) {
        stage.setTitle("Temperature Change in Greece");
        
        int minYear = Integer.MAX_VALUE;
        int maxYear = Integer.MIN_VALUE;

        // Fetch the min and max years from the database
        try {
            int[] yearRange = fetchYearRange();
            minYear = yearRange[0];
            maxYear = yearRange[1];
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Defining the x and y axes
        final NumberAxis xAxis = new NumberAxis(minYear, maxYear, 1);
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Year");
        yAxis.setLabel("Temperature Change (°C)");

        // Creating the line chart
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Annual Surface Temperature Change in Greece");

        // Defining the data series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Greece");

        // Fetch data from the database and add to series
        fetchData(series);

        // Adding data to the chart
        lineChart.getData().add(series);

        // Setting up the scene and showing the stage
        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void fetchData(XYChart.Series<Number, Number> series) {
        String url = "jdbc:mysql://localhost:3306/countriesData"; // Replace with your database name
        String user = "root"; // Replace with your MySQL username
        String password = "s1234taE!5678"; // Replace with your MySQL password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT yearChange, valueChange FROM temperatureChange " +
                     "WHERE country = 'Greece' " +
                     "ORDER BY yearChange")) {

            while (rs.next()) {
                String yearStr = rs.getString("yearChange").substring(1); // Remove 'F' prefix
                int year = Integer.parseInt(yearStr);
                double value = rs.getDouble("valueChange");
                series.getData().add(new XYChart.Data<>(year, value));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private int[] fetchYearRange() throws Exception {
        String url = "jdbc:mysql://localhost:3306/countriesData"; // Replace with your database name
        String user = "root"; // Replace with your MySQL username
        String password = "s1234taE!5678"; // Replace with your MySQL password

        int minYear = Integer.MAX_VALUE;
        int maxYear = Integer.MIN_VALUE;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT MIN(SUBSTRING(yearChange, 2) + 0) as minYear, " +
                     "MAX(SUBSTRING(yearChange, 2) + 0) as maxYear " +
                     "FROM temperatureChange WHERE country = 'Greece' ")) {

            if (rs.next()) {
                minYear = rs.getInt("minYear");
                maxYear = rs.getInt("maxYear");
            }

        } catch (Exception e) {
            throw e;
        }

        return new int[]{minYear, maxYear};
    }

    public static void main(String[] args) {
        launch(args);
    }
}
