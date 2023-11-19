package au10tixServer;

import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.Response;
import replayes.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Au10tixRestClient {

    public static String baseUrl;
    public static String processUrl;
    public static String serialId1;
    public static String serialId2;

    public static replayes.Response serial1;
    public static replayes.Response serial2;
    public static Utilities utilities;


//    public static int serial;
//    public static String date;
//    public static String version;



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
            processUrl = props.getProperty("processUrl");
//            serial = Integer.parseInt(props.getProperty("serial"));
//            date = props.getProperty("date");
//            version = props.getProperty("version");
        } catch (Exception e) {
            System.out.println("There was problem load the properties file");
        }
    }

    public static void main(String[] args) throws IOException {
        // initialize Base URL and serial IDs for JSON reply by serial ID
        initialize();
        utilities.initializeUtils();

        // Send request for Serial ID 1
        System.out.println("Response serial 1 : " + utilities.getResponse(baseUrl + serialId1).body().string());
        serial1 = utilities.getResponseJSON(baseUrl + serialId1);

        // Send request for Serial ID 2
        System.out.println("Response serial 2 : " + utilities.getResponse(baseUrl + serialId2).body().string());
        serial2 = utilities.getResponseJSON(baseUrl + serialId2);

        // process Url
        System.out.println("process Url: "+utilities.postResponse(utilities.getAndBuildResponseObject(serial1, serial2), processUrl).body().string());

    }


}