package au10tixServer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Au10tixProcessJsonClient {

    public static void main(String[] args) {
        // Base URL for JSON process endpoint
        String processUrl = "https://resttest10.herokuapp.com/api/process";

        // Create a JSON payload
        String jsonPayload = createJsonPayload();

        // Create an instance of HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Send the JSON payload to be processed
        sendProcessRequest(httpClient, processUrl, jsonPayload);
    }

    private static String createJsonPayload() {
        // Constructing the JSON payload according to the specified requirements
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("serial", 3);

        Map<String, Object> messageMap = new HashMap<>();
        Map<String, Object> subsetMap = new HashMap<>();
        Map<String, Object> generalMap = new HashMap<>();

        generalMap.put("information", Map.of(
                "date", "1-2-2021",
                "version", "3.00"
        ));

        // Assuming you have the higher values available, replace them with actual values
//        generalMap.put("quantities", Map.of(
//                "first",  /* Set to the higher value from the two JSON replies */,
//                "second", /* Set to the higher value from the two JSON replies */,
//                "third",  /* Set to the higher value from the two JSON replies */
//                "fourth", "some_value"  // Add any other necessary fields
//        ));

        subsetMap.put("general", generalMap);
        messageMap.put("subset", subsetMap);
        jsonMap.put("message", messageMap);

        // Convert the JSON map to a JSON string
        return mapToJsonString(jsonMap);
    }

    private static String mapToJsonString(Map<String, Object> map) {
        // Convert a Map to a JSON string
        StringBuilder jsonString = new StringBuilder("{");
        map.forEach((key, value) -> jsonString.append("\"").append(key).append("\":").append(formatValue(value)).append(","));
        jsonString.deleteCharAt(jsonString.length() - 1); // Remove the trailing comma
        jsonString.append("}");
        return jsonString.toString();
    }

    private static String formatValue(Object value) {
        // Format the value for JSON representation
        if (value instanceof String) {
            return "\"" + value + "\"";
        } else {
            return value.toString();
        }
    }

    private static void sendProcessRequest(HttpClient httpClient, String url, String jsonPayload) {
        // Create an instance of HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

        try {
            // Send the request and receive the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response body
            System.out.println("Response for " + url + ":");
            System.out.println(response.body());
            System.out.println("----------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
