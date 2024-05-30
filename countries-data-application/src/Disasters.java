import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class Disasters extends Application{
	private List<String> countries;
    private int startYear;
    private int endYear;
    private String chartType;
    private String disasterType;

    public Disasters(List<String> countries, int startYear, int endYear, String chartType, String disasterType) {
        this.countries = countries;
        this.startYear = startYear;
        this.endYear = endYear;
        this.chartType = chartType;
        this.disasterType = "Climate related disasters frequency, Number of Disasters: " + disasterType;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Climate Disasters");

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

        chart.setTitle(disasterType);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(disasterType);

        String url = "jdbc:mysql://localhost:3306/countriesData";
        String user = "root";
        String password = "s1234taE!5678";

        try (Connection conn = DriverManager.getConnection(url, user, password);
               Statement stmt = conn.createStatement()) {

               for (String country : countries) {
                   String query = "SELECT yearChange, valueChange FROM climateDisasters WHERE country = '" + country + "' AND indicatorC = '" + disasterType + "' AND SUBSTRING(yearChange, 2) + 0 BETWEEN " + startYear + " AND " + endYear + " ORDER BY yearChange";
                   ResultSet rs = stmt.executeQuery(query);

                   XYChart.Series<String, Number> seriesD = new XYChart.Series<>();
                   seriesD.setName(country);

                   while (rs.next()) {
                       seriesD.getData().add(new XYChart.Data<>(rs.getString("yearChange"), rs.getDouble("valueChange")));
                   }

                   chart.getData().add(seriesD);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }

        VBox vbox = new VBox(chart);
        Scene scene = new Scene(vbox, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*public static void main(String[] args) {
        launch(args);
    }*/
}
