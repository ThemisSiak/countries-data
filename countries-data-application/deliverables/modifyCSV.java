import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.io.*;
import java.util.*;

public class modifyCSV{
	public static final int COLUMN_NUMBER = 10;
	
	public static void main(String[] args){
		try{
			String[] csvFiles = {"countries.csv", "Annual_Surface_Temperature_Change.csv", "Climate-related_Disasters_Frequency.csv", "Land_Cover_Accounts.csv", "Forest_and_Carbon.csv"};
			
			//Keep only the common Countries in the CSV Files
			Set<String> commonCountries = new HashSet<>();
			
			commonCountries.addAll(getUniqueCountries(csvFiles[0], 1));
			
			for(int i=1; i<csvFiles.length; i++){
				Set<String> currentCountries = getUniqueCountries(csvFiles[i], 3);
				commonCountries.retainAll(currentCountries);
			}
			
			for(int i=0; i<csvFiles.length; i++){
				int column = 3;
				if (i==0){
					column = 1;
				}
				else{
					column = 3;
				}
				removeExtraCountries(csvFiles[i], commonCountries, column);
			}
			
			//Add Column ISO_CODE from countries.csv to all the others csv files
			//and delete ObjectID column
			
			//ISO3 +  ISO_Code
			String[] infoCountries = getColumnData(csvFiles[0], 1, 2);
			
			for (int i=1; i<csvFiles.length; i++){
				addColumnData(csvFiles[i], infoCountries[0], infoCountries[1], commonCountries);
			}
			
			//change the columns of years to one column
			int columnNum;
			for(String csvFile: csvFiles){
				columnNum = COLUMN_NUMBER;
				if(!csvFile.equals(csvFiles[0])){
					if(csvFile.equals(csvFiles[3])){
						columnNum = 11;
					}
					modifyYears(csvFile, columnNum);
				}
			}
			
		} catch (IOException e){
			e.printStackTrace();
		}	
	}
	
	private static Set<String> getUniqueCountries(String csvFile, int column) throws IOException{
		Set<String> uniqueCountries = new HashSet<>();
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		String line;
		while ((line = reader.readLine()) != null){
			String[] parts = line.split(",");
			uniqueCountries.add(parts[column]);
		}
		reader.close();
		return uniqueCountries;
	}
	
	private static void removeExtraCountries(String csvFile, Set<String> commonCountries, int column) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		FileWriter writer = new FileWriter("temp.csv");
		String line;
		
		while((line = reader.readLine())!= null){
			String[] parts = line.split(",");
			if(commonCountries.contains(parts[column])){
				writer.write(line + System.lineSeparator());
			}
		}
		reader.close();
		writer.close();
		
		java.nio.file.Files.move(java.nio.file.Paths.get("temp.csv"), java.nio.file.Paths.get(csvFile), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
	}
	
	private static String[] getColumnData(String csvFile, int column1, int column2) throws IOException {
		StringBuilder columnData1 = new StringBuilder();
		StringBuilder columnData2 = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		String line;
		while((line = reader.readLine())!=null){
			String[] parts = line.split(",");
			columnData1.append(parts[column1]).append("\n");
			columnData2.append(parts[column2]).append("\n");
		}
		reader.close();
		return new String[]{columnData1.toString(), columnData2.toString()};
	}
	
	private static void addColumnData(String csvFile, String columnData1, String columnData2, Set<String> commonCountries) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		FileWriter writer = new FileWriter("temp.csv");
		String line;
		int lineNum = 0;
		
		String[] column1Parts = columnData1.split("\n");
        String[] column2Parts = columnData2.split("\n");
		while((line=reader.readLine()) !=null){
			String[] parts = line.split(",");
			if(commonCountries.contains(parts[3])){
				int matchingIndex = -1;
				for(int i=0; i<column1Parts.length; i++){
					if(column1Parts[i].equals(parts[3])){
						matchingIndex = i;
						break;
					}
				}
				if (matchingIndex != -1 && matchingIndex < column2Parts.length) {
                    writer.write(column2Parts[matchingIndex] + "," + parts[1] + "," + String.join(",", Arrays.copyOfRange(parts, 2, parts.length)) + System.lineSeparator());
                } else {
                    writer.write("," + parts[1] + "," + String.join(",", Arrays.copyOfRange(parts, 2, parts.length)) + System.lineSeparator());
                }
			}
		}
		reader.close();
		writer.close();
		
		java.nio.file.Files.move(java.nio.file.Paths.get("temp.csv"), java.nio.file.Paths.get(csvFile), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
	}
	
	// Custom CSV parsing logic to handle quoted fields
    private static List<String> parseCSVLine(String line) {
        List<String> parts = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentPart = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                currentPart.append(c); // Include quotes in the part
            } else if (c == ',' && !inQuotes) {
                parts.add(currentPart.toString());
                currentPart.setLength(0);
            } else {
                currentPart.append(c);
            }
        }
        parts.add(currentPart.toString()); // Add the last part
        return parts;
    }

    private static void modifyYears(String csvFile, int columnNum) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(csvFile));
        FileWriter writer = new FileWriter("temp.csv");
        String line;
        int lineCounter = 0;

        List<String> yearHeaders = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            List<String> parts = parseCSVLine(line);
            if (lineCounter == 0) {
                StringBuilder headerLine = new StringBuilder();
                for (int i = 0; i < Math.min(columnNum, parts.size()); i++) {
                    if (i > 0) {
                        headerLine.append(",");
                    }
                    headerLine.append(parts.get(i));
                }
                headerLine.append(",Year,Value");
                writer.write(headerLine.toString() + System.lineSeparator());

                for (int i = columnNum; i < parts.size(); i++) {
                    yearHeaders.add(parts.get(i));
                }
            } else { // Data rows
                StringBuilder baseLine = new StringBuilder();
                for (int i = 0; i < columnNum; i++) {
                    if (i < parts.size()) {
                        baseLine.append(parts.get(i));
                    }
                    baseLine.append(",");
                }

                int year = 0;
                for (int i = columnNum; i < parts.size(); i++) {
                    String yearValue = yearHeaders.get(year);
                    String dataValue = parts.get(i).isEmpty() ? " " : parts.get(i);
                    writer.write(baseLine.toString() + yearValue + "," + dataValue + System.lineSeparator());
                    year++;
                }

                // Handle case where there are fewer columns than expected
                if (parts.size() < columnNum) {
                    for (int i = parts.size(); i < columnNum; i++) {
                        baseLine.append(",");
                    }
                    writer.write(baseLine.toString() + "Year,Value" + System.lineSeparator());
                }
            }
            lineCounter++;
        }
        reader.close();
        writer.close();

        java.nio.file.Files.move(java.nio.file.Paths.get("temp.csv"), java.nio.file.Paths.get(csvFile), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }
}