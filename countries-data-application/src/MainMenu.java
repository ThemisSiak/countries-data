import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

import java.util.List;

public class MainMenu extends Application{
	private ComboBox<String> countryComboBox;
    private ComboBox<String> queryComboBox;
    private ComboBox<Integer> startYearComboBox;
    private ComboBox<Integer> endYearComboBox;
    private ComboBox<String> chartTypeComboBox;
    private ComboBox<String> disasterTypeComboBox;
    private ListView<String> countryListView;

    private int minYear = Integer.MAX_VALUE;
    private int maxYear = Integer.MIN_VALUE;

    @Override
    public void start(Stage primaryStage) {
    	primaryStage.setTitle("Main Menu");

        countryComboBox = new ComboBox<>();
        queryComboBox = new ComboBox<>();
        startYearComboBox = new ComboBox<>();
        endYearComboBox = new ComboBox<>();
        chartTypeComboBox = new ComboBox<>();
        disasterTypeComboBox = new ComboBox<>();
        countryListView = new ListView<>();
        countryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        queryComboBox.getItems().addAll("Area (sq km)", "Population", "Temperature Change", "Disasters", "Land Cover", "Forest Carbon");
        chartTypeComboBox.getItems().addAll("Line Chart", "Bar Chart", "Scatter Plot");
        disasterTypeComboBox.getItems().addAll("Drought", "Extreme Temperature", "Flood", "Landslide", "Storm", "TOTAL", "Wildfire");

        queryComboBox.setOnAction(e -> {
            String query = queryComboBox.getValue();
            boolean isCountryMetric = query.equals("Area (sq km)") || query.equals("Population");
            startYearComboBox.setDisable(isCountryMetric);
            endYearComboBox.setDisable(isCountryMetric);
            disasterTypeComboBox.setDisable(!query.equals("Disasters"));
            countryComboBox.setDisable(isCountryMetric);
            countryListView.setDisable(!isCountryMetric);
        });

        getCountriesAndYears();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> showVisualization());

        VBox vbox = new VBox(10, countryComboBox, queryComboBox, startYearComboBox, endYearComboBox, chartTypeComboBox, disasterTypeComboBox, countryListView, submitButton);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 300, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void getCountriesAndYears() {
        String url = "jdbc:mysql://localhost:3306/countriesData";
        String user = "root";
        String password = "s1234taE!5678";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT DISTINCT displayName FROM countries ORDER BY displayName");
            while (rs.next()) {
                countryComboBox.getItems().add(rs.getString("displayName"));
                countryListView.getItems().add(rs.getString("displayName"));
            }

            rs = stmt.executeQuery(
                "SELECT MIN(SUBSTRING(yearChange, 2) + 0) as minYear, " +
                "MAX(SUBSTRING(yearChange, 2) + 0) as maxYear " +
                "FROM temperatureChange");

            if (rs.next()) {
                minYear = rs.getInt("minYear");
                maxYear = rs.getInt("maxYear");
            }

            for (int year = minYear; year <= maxYear; year++) {
                startYearComboBox.getItems().add(year);
                endYearComboBox.getItems().add(year);
            }

            startYearComboBox.setValue(minYear);
            endYearComboBox.setValue(maxYear);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showVisualization() {
    	String query = queryComboBox.getValue();
        String chartType = chartTypeComboBox.getValue();

        if (query == null || chartType == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please make all selections.");
            alert.show();
            return;
        }

        if (query.equals("Area (sq km)") || query.equals("Population")) {
            List<String> countries = countryListView.getSelectionModel().getSelectedItems();
            if (countries.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select at least one country.");
                alert.show();
                return;
            }
            new Countries(countries, query, chartType).start(new Stage());
        } else {
            String country = countryComboBox.getValue();
            int startYear = startYearComboBox.getValue();
            int endYear = endYearComboBox.getValue();
            String disasterType = disasterTypeComboBox.getValue();

            if (country == null || startYear == 0 || endYear == 0 ||
                ("Disasters".equals(query) && disasterType == null)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please make all selections.");
                alert.show();
                return;
            }

            switch (query) {
                case "Temperature Change":
                    new TemperatureChange(country, startYear, endYear, chartType).start(new Stage());
                    break;
                case "Disasters":
                    new Disasters(country, startYear, endYear, chartType, disasterType).start(new Stage());
                    break;
                case "Land Cover":
                    new LandCover(country, startYear, endYear, chartType).start(new Stage());
                    break;
                case "Forest Carbon":
                    new ForestCarbon(country, startYear, endYear, chartType).start(new Stage());
                    break;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
