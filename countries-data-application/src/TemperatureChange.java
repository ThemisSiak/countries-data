import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TemperatureChange extends Application{
	private String country;
    private int startYear;
    private int endYear;
    private String chartType;

    public TemperatureChange(String country, int startYear, int endYear, String chartType) {
        this.country = country;
        this.startYear = startYear;
        this.endYear = endYear;
        this.chartType = chartType;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Temperature Change Visualization");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        XYChart<String, Number> chart;
        switch (chartType) {
            case "Bar Chart":
                chart = new BarChart<>(xAxis, yAxis);
                break;
            case "Scatter Plot":
                chart = new ScatterChart<>(xAxis, yAxis);
                break;
            case "Line Chart":
            default:
                chart = new LineChart<>(xAxis, yAxis);
                break;
        }
        
        chart.setTitle("Temperature Change in " + country);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Temperature Change");

        String url = "jdbc:mysql://localhost:3306/countriesData";
        String user = "root";
        String password = "s1234taE!5678";

        String query = "SELECT SUBSTRING(yearChange, 2) + 0 as year, valueChange " +
                       "FROM temperatureChange WHERE country = '" + country + "' " +
                       "AND (SUBSTRING(yearChange, 2) + 0) BETWEEN " + startYear + " AND " + endYear + 
                       " ORDER BY year";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int year = rs.getInt("year");
                double valueChange = rs.getDouble("valueChange");
                series.getData().add(new XYChart.Data<>(String.valueOf(year), valueChange));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        chart.getData().add(series);

        VBox vbox = new VBox(chart);
        Scene scene = new Scene(vbox, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*public static void main(String[] args) {
        launch(args);
    }*/
}
