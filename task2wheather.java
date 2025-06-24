import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleWeatherApp {

    // Replace with your OpenWeatherMap API key
    private static final String API_KEY = "your_actual_key";

    public static void main(String[] args) {
        String city = "Mumbai";
        fetchWeather(city);
    }

    public static void fetchWeather(String city) {
        try {
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + API_KEY + "&units=metric";

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder json = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();

            // Convert JSON string to simple strings (basic extraction)
            String response = json.toString();

            // Extract city name
            String cityName = extractValue(response, "\"name\":\"", "\"");

            // Extract temperature
            String tempStr = extractValue(response, "\"temp\":", ",");
            double temperature = Double.parseDouble(tempStr);

            // Extract humidity
            String humidityStr = extractValue(response, "\"humidity\":", "}");
            int humidity = Integer.parseInt(humidityStr);

            // Extract weather description
            String description = extractValue(response, "\"description\":\"", "\"");

            // Display result
            System.out.println("\nWeather Report for: " + cityName);
            System.out.println("-----------------------------");
            System.out.println("Temperature : " + temperature + " °C");
            System.out.println("Humidity    : " + humidity + " %");
            System.out.println("Condition   : " + description);

        } catch (Exception e) {
            System.out.println("Error fetching weather: " + e.getMessage());
        }
    }

    // Basic utility to extract value from JSON-like string
    public static String extractValue(String json, String keyStart, String keyEnd) {
        int start = json.indexOf(keyStart);
        if (start == -1) return "N/A";
        start += keyStart.length();
        int end = json.indexOf(keyEnd, start);
        if (end == -1) return "N/A";
        return json.substring(start, end);
    }
}