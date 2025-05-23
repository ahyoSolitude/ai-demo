package com.example.aidemo.tools;

import com.example.aidemo.model.ChatMessage;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class WeatherTools implements Tool {

    private final Random random = new Random();
    private final Map<String, WeatherData> cityWeatherCache = new HashMap<>();

    @Override
    public String getName() {
        return "getWeather";
    }

    @Override
    public String getDescription() {
        return "Get the current weather for a specific location";
    }

    @Override
    public List<ToolParameter> getParameters() {
        return List.of(
            new ToolParameter("location", "string", "The city and state, e.g. San Francisco, CA", true)
        );
    }

    @Override
    public String invoke(ChatMessage userMessage, Map<String, Object> parameters) {
        try {
            String location = (String) parameters.get("location");
            WeatherData weatherData = getWeatherForLocation(location);
            
            return String.format(
                "Weather in %s: %s, Temperature: %.1f°C, Humidity: %d%%, Wind: %.1f km/h",
                location, weatherData.getCondition(), weatherData.getTemperature(), 
                weatherData.getHumidity(), weatherData.getWindSpeed()
            );
        } catch (Exception e) {
            return "Error getting weather data: " + e.getMessage();
        }
    }
    
    private WeatherData getWeatherForLocation(String location) {
        // Check if we already have weather data for this location
        if (cityWeatherCache.containsKey(location)) {
            return cityWeatherCache.get(location);
        }
        
        // Simulate weather API call with random data
        String[] conditions = {"Sunny", "Cloudy", "Rainy", "Partly Cloudy", "Thunderstorm", "Snowy", "Foggy"};
        String condition = conditions[random.nextInt(conditions.length)];
        
        double temperature = 10 + random.nextDouble() * 25; // Temperature between 10°C and 35°C
        int humidity = 30 + random.nextInt(60); // Humidity between 30% and 90%
        double windSpeed = random.nextDouble() * 30; // Wind speed between 0 and 30 km/h
        
        WeatherData weatherData = new WeatherData(condition, temperature, humidity, windSpeed);
        
        // Cache the result
        cityWeatherCache.put(location, weatherData);
        
        return weatherData;
    }
    
    @Data
    private static class WeatherData {
        private final String condition;
        private final double temperature;
        private final int humidity;
        private final double windSpeed;
    }
}