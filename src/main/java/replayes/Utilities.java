package replayes;

import au10tixServer.Au10tixRestClient;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utilities {

    public static General general;
    public static Information information;
    public static Message message;
    public static Quantities quantities;
    public static Subset subset;
    public static String jsonBody;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static okhttp3.Response response;
    public static Request request;
    public static OkHttpClient client = new OkHttpClient();
    public static String respBody;
    public static replayes.Response responseJson;
    public static int serial;
    public static String date;
    public static String version;
    public static String baseUrl;
    public static String processUrl;
    public static String serialId1;
    public static String serialId2;

    public static void initializeUtils() {
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
            serial = Integer.parseInt(props.getProperty("serial"));
            date = props.getProperty("date");
            version = props.getProperty("version");
        } catch (Exception e) {
            System.out.println("There was problem load the properties file");
        }
    }

    // get response
    public static okhttp3.Response getResponse(String address) throws IOException {
        request = new Request.Builder().url(address).build();
        return client.newCall(request).execute();
    }

    // get response as JSON
    public static replayes.Response getResponseJSON(String address) throws IOException {
        respBody = getResponse(address).body().string();
        responseJson = new Gson().fromJson(respBody, replayes.Response.class);
        return responseJson;
    }

    // post the request
    public static okhttp3.Response postResponse(replayes.Response respObj, String address) throws IOException {
        jsonBody = new Gson().toJson(respObj);
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder().url(address).post(body).build();
        response = client.newCall(request).execute();
        return response;
    }

    // build the request itself
    public static replayes.Response getAndBuildResponseObject(replayes.Response serial1, replayes.Response serial2){
        int res1 , res2, res3;
        int firstSerial1 = serial1.getMessage().getSubset().getGeneral().getQuantities().getFirst();
        int firstSerial2 = serial2.getMessage().getSubset().getGeneral().getQuantities().getFirst();
        int secondSerial1 = serial1.getMessage().getSubset().getGeneral().getQuantities().getSecond();
        int secondSerial2 = serial2.getMessage().getSubset().getGeneral().getQuantities().getSecond();
        int thirdSerial1 = serial1.getMessage().getSubset().getGeneral().getQuantities().getThird();
        int thirdSerial2 = serial2.getMessage().getSubset().getGeneral().getQuantities().getThird();

        res1 = Math.max(firstSerial1, firstSerial2);
        res2 = Math.max(secondSerial1, secondSerial2);
        res3 = Math.max(thirdSerial1, thirdSerial2);

        general = new General();
        information = new Information();
        message = new Message();
        subset = new Subset();

        quantities = new Quantities();
        quantities.setFirst(res1);
        quantities.setSecond(res2);
        quantities.setThird(res3);

        information.setDate(date);
        information.setVersion(version);

        general.setQuantities(quantities);
        general.setInformation(information);

        subset.setGeneral(general);

        message.setSubset(subset);

        responseJson = new replayes.Response(serial, message);

        return responseJson;
    }
}
