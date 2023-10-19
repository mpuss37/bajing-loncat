package org.example;

import java.io.*;
import java.net.HttpURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GetDataResource {
    private URL url;
    private HttpURLConnection httpURLConnection;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;
    private JSONObject jsonObjectObj1, jsonObjectObj2;
    private JSONArray jsonArray;

    void setApiUrl(String paramQuery, int parampAmount, String paramPageUrl) {
        try {
            String apiUrl = "https://unsplash.com/napi/search/photos?query=" + paramQuery + "&per_page=" + parampAmount + "&page=" + paramPageUrl;
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
            setValueAmountJson(paramQuery,parampAmount);
        } catch (IOException e) {
            System.out.println("check your connection");
        }
    }

    private int setValueAmountJson(String paramQuery, int valueAmountJson) {
        //use for set value param page
        int j = 0;
        while (j <= (valueAmountJson - 1)) {
            setJsonParsing(paramQuery,stringBuilder, j, "full");
            j++;
        }
        return 0;
    }

    private void setWriteFile(String paramUrlImage, String paramNameImage, int paramNumberSubNameFile){
        try {
            url = new URL(paramUrlImage);
            String namaFile = "/home/mpuss/Pictures/gajah/"+paramNameImage+paramNumberSubNameFile+".jpg";
            try (InputStream in = new BufferedInputStream(url.openStream());
                 FileOutputStream fos = new FileOutputStream(namaFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                System.out.println("File finish to save " + namaFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            System.out.println("check your connection");
        }
    }

    private void setJsonParsing(String paramNameFileQuery, StringBuilder paramStringBuilder, int paramIndexJson, String paramGetVariation) {
        //parsing json to string
        jsonObjectObj1 = new JSONObject(paramStringBuilder.toString());
        jsonArray = jsonObjectObj1.getJSONArray("results");
        jsonObjectObj1 = jsonArray.getJSONObject(paramIndexJson);
        jsonObjectObj2 = jsonObjectObj1.getJSONObject("urls");
        String variation = jsonObjectObj2.getString(paramGetVariation);
        //for getString obj2 with key : urls
        int numberSubNameImg = paramIndexJson + 1;
        //for set file name to add 'filename[number].jpg'
        setWriteFile(variation,paramNameFileQuery,numberSubNameImg);
        System.out.println(paramNameFileQuery+"Nomor " + numberSubNameImg);
    }
}
