package com.bajingloncat.repository;

import com.bajingloncat.config.AppConfig;
import com.bajingloncat.dto.SearchResult;
import com.bajingloncat.exception.ApiException;
import com.bajingloncat.model.Photo;
import com.bajingloncat.security.CookieManager;
import org.brotli.dec.BrotliInputStream;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class UnsplashRepository {

    private final CookieManager cookieManager;

    public UnsplashRepository() {
        this.cookieManager = new CookieManager();
    }

    public SearchResult search(String query, int page, int perPage) {
        String cookie = cookieManager.getValidCookie();
        return searchWithCookie(query, page, perPage, cookie, true);
    }

    private SearchResult searchWithCookie(String query, int page, int perPage, String cookie, boolean retry) {
        try {
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            String apiUrl = AppConfig.NAPI_SEARCH_URL
                    + "?query=" + encodedQuery
                    + "&per_page=" + perPage
                    + "&page=" + page;

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(AppConfig.CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(AppConfig.READ_TIMEOUT_MS);

            conn.setRequestProperty("User-Agent", AppConfig.USER_AGENT);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            conn.setRequestProperty("Cookie", cookie);

            // Debug: print cookie length
            System.out.println("    [debug] Cookie length: " + cookie.length());

            int status = conn.getResponseCode();

            if ((status == 401 || status == 403) && retry) {
                System.out.println("    ⚠️  Cookie expired, refresh...");
                String newCookie = cookieManager.refreshCookie();
                return searchWithCookie(query, page, perPage, newCookie, false);
            }

            if (status != 200) {
                throw new ApiException("API returned HTTP " + status, status);
            }

            String encoding = conn.getContentEncoding();
            InputStream rawStream = conn.getInputStream();
            InputStream decoded;
            if ("br".equalsIgnoreCase(encoding)) {
                decoded = new BrotliInputStream(rawStream);
            } else if ("gzip".equalsIgnoreCase(encoding)) {
                decoded = new GZIPInputStream(rawStream);
            } else {
                decoded = rawStream;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(decoded, "UTF-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String res = response.toString();
            if (!res.startsWith("{")) {
                throw new ApiException("Response bukan JSON");
            }

            JSONObject json = new JSONObject(res);
            return SearchResult.fromJson(json);

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Gagal fetch: " + e.getMessage());
        }
    }

    public List<Photo> getRandomPhotos(int jumlah) {
        System.out.println("  📡 Fetching random dari Unsplash...");

        List<Photo> allPhotos = new ArrayList<>();
        int page = 1;

        while (allPhotos.size() < jumlah && page <= AppConfig.MAX_PAGES) {
            SearchResult result = search("random", page, AppConfig.PER_PAGE);
            allPhotos.addAll(result.getResults());

            System.out.println("    Page " + page + ": " + result.getResultCount() + " foto (total: " + allPhotos.size() + ")");

            if (result.getResultCount() == 0) {
                break;
            }
            page++;
        }

        Collections.shuffle(allPhotos);

        if (allPhotos.size() > jumlah) {
            return allPhotos.subList(0, jumlah);
        }
        return allPhotos;
    }

    public List<Photo> searchPhotos(String query, int jumlah) {
        System.out.println("  📡 Mencari \"" + query + "\" di Unsplash...");

        List<Photo> allPhotos = new ArrayList<>();
        int page = 1;

        while (allPhotos.size() < jumlah && page <= AppConfig.MAX_PAGES) {
            SearchResult result = search(query, page, AppConfig.PER_PAGE);
            allPhotos.addAll(result.getResults());

            System.out.println("    Page " + page + ": " + result.getResultCount() + " foto (total: " + allPhotos.size() + ")");

            if (result.getResultCount() == 0) {
                break;
            }
            page++;
        }

        Collections.shuffle(allPhotos);

        if (allPhotos.size() > jumlah) {
            return allPhotos.subList(0, jumlah);
        }
        return allPhotos;
    }
}
