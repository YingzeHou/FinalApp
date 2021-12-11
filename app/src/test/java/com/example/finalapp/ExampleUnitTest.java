package com.example.finalapp;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void weatherApi() throws IOException, JSONException {
        String url = "https://api.openweathermap.org/data/2.5/weather?" +
                "lat=43.073051&lon=-89.401230&appid=e3cf888202a062bf7e293eadce4af378";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //Read JSON response and print
        JSONObject myResponse = new JSONObject(response.toString());
        double temp = myResponse.getJSONObject("main").getDouble("feels_like")-273.15;
        BigDecimal bd = new BigDecimal(temp).setScale(2, RoundingMode.HALF_UP);
        temp = bd.doubleValue();
        System.out.println("Weather: Feels like "+temp+"℃");
//        return "Weather: Feels like "+temp+"℃";
    }
}