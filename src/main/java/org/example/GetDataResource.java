package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;

public class GetDataResource {
    private URL url;
    private HttpURLConnection httpURLConnection;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;
    private JSONObject jsonObject;
    private JSONArray jsonArray;

    void setApiUrl(String paramQuery, String parampAmount, String paramPageUrl) throws IOException {
        String apiUrl = "https://unsplash.com/napi/search/photos?query="+paramQuery+"&per_page="+parampAmount+"&page="+paramPageUrl;
        url = new URL(apiUrl);
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        stringBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        bufferedReader.close();
        httpURLConnection.disconnect();
        setJsonParsing(stringBuilder,"color");
//        jsonObject = new JSONObject(stringBuilder.toString());
//        jsonArray = jsonObject.getJSONArray("results");
//        JSONObject firstMatch = jsonArray.getJSONObject(0);
    }

    void setJsonParsing(StringBuilder paramStringBuilder, String paramGetStringJson){
        jsonObject = new JSONObject(paramStringBuilder.toString());
        jsonArray = jsonObject.getJSONArray("results");
        jsonObject = jsonArray.getJSONObject(0);
        paramGetStringJson = jsonObject.getString("segment");
        System.out.println(paramGetStringJson);
    }
}
