package au10tixServer;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class Au10tixRestClient {

    private static String baseUrl;
    private static String serialId1;
    private static String serialId2;

    public static void initialize() {
        try {
            // load properties
            Properties props = new Properties();

            String propFileName = "config.properties";
            // get the config properties file
            InputStream inputStream = Au10tixRestClient.class.getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);
            baseUrl = props.getProperty("baseUrl");
            serialId1 = props.getProperty("serialId1");
            serialId2 = props.getProperty("serialId2");
        } catch (Exception e) {
            System.out.println("There was problem load the properties file");
        }
    }

    public static void main(String[] args) {
        // initialize Base URL and serial IDs for JSON reply by serial ID
        initialize();

        // Create an instance of HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Send request for Serial ID 1
        sendRequest(httpClient, baseUrl + serialId1);

        // Send request for Serial ID 2
        sendRequest(httpClient, baseUrl + serialId2);
    }

    private static void sendRequest(HttpClient httpClient, String url) {
        // Create an instance of HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
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