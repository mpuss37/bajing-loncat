package com.bajingloncat.dto;

import com.bajingloncat.model.Photo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

    private int total;
    private int totalPages;
    private int perPage;
    private List<Photo> results;

    public SearchResult() {
        this.results = new ArrayList<>();
    }

    public SearchResult(int total, int totalPages, List<Photo> results) {
        this.total = total;
        this.totalPages = totalPages;
        this.results = results;
    }

    public static SearchResult fromJson(JSONObject json) {
        SearchResult result = new SearchResult();
        result.total = json.optInt("total", 0);
        result.totalPages = json.optInt("total_pages", 0);

        JSONArray resultsArray = json.optJSONArray("results");
        if (resultsArray != null) {
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject photoJson = resultsArray.getJSONObject(i);
                Photo photo = parsePhoto(photoJson);
                if (photo != null) {
                    result.results.add(photo);
                }
            }
        }

        return result;
    }

    private static Photo parsePhoto(JSONObject json) {
        try {
            String id = json.getString("id");

            JSONObject urls = json.getJSONObject("urls");
            String urlFull = urls.getString("full");
            String urlThumb = urls.optString("thumb", "");

            int width = json.optInt("width", 0);
            int height = json.optInt("height", 0);
            String description = json.optString("description", "");
            if (description.isEmpty()) {
                description = json.optString("alt_description", "");
            }

            String author = "";
            JSONObject user = json.optJSONObject("user");
            if (user != null) {
                author = user.optString("name", "");
            }

            Photo photo = new Photo(id, urlFull);
            photo.setUrlThumb(urlThumb);
            photo.setWidth(width);
            photo.setHeight(height);
            photo.setDescription(description);
            photo.setAuthor(author);

            return photo;
        } catch (Exception e) {
            return null;
        }
    }

    public int getTotal() { return total; }
    public int getTotalPages() { return totalPages; }
    public int getPerPage() { return perPage; }
    public List<Photo> getResults() { return results; }
    public int getResultCount() { return results.size(); }
}
