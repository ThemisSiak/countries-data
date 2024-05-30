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

public class Countries extends Application{
	private List<String> countries;
    private String metric;
    private String chartType;

    public Countries(List<String> countries, String metric, String chartType) {
        this.countries = countries;
        this.metric = metric;
        this.chartType = chartType;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Country Statistics Visualization");

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

        chart.setTitle(metric + " of Selected Countries");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(metric);

        String url = "jdbc:mysql://localhost:3306/countriesData";
        String user = "root";
        String password = "s1234taE!5678";

        String column = metric.equals("Area (sq km)") ? "AreaSqKm" : "Population";

        StringBuilder queryBuilder = new StringBuilder("SELECT displayName, " + column + " FROM countries WHERE displayName IN (");
        for (int i = 0; i < countries.size(); i++) {
            queryBuilder.append("'").append(countries.get(i)).append("'");
            if (i < countries.size() - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(")");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(queryBuilder.toString())) {

            while (rs.next()) {
                String country = rs.getString("DisplayName");
                double value = rs.getDouble(column);
                series.getData().add(new XYChart.Data<>(country, value));
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
