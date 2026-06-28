package com.bajingloncat.util;

import com.bajingloncat.config.AppConfig;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtils {

    private FileUtils() {}

    public static void downloadImage(String imageUrl, java.io.File outputFile) throws Exception {
        URL url = new URL(imageUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", AppConfig.USER_AGENT);
        conn.setConnectTimeout(AppConfig.CONNECT_TIMEOUT_MS);
        conn.setReadTimeout(AppConfig.READ_TIMEOUT_MS);
        conn.setInstanceFollowRedirects(true);

        int status = conn.getResponseCode();
        if (status != 200) {
            throw new Exception("HTTP " + status);
        }

        try (InputStream in = conn.getInputStream();
             FileOutputStream out = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[AppConfig.DOWNLOAD_BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    public static String sanitizeFilename(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
