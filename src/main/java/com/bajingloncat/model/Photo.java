package com.bajingloncat.model;

public class Photo {

    private String id;
    private String urlFull;
    private String urlThumb;
    private int width;
    private int height;
    private String description;
    private String author;

    public Photo() {}

    public Photo(String id, String urlFull) {
        this.id = id;
        this.urlFull = urlFull;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUrlFull() { return urlFull; }
    public void setUrlFull(String urlFull) { this.urlFull = urlFull; }

    public String getUrlThumb() { return urlThumb; }
    public void setUrlThumb(String urlThumb) { this.urlThumb = urlThumb; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    @Override
    public String toString() {
        return "Photo{id='" + id + "', urlFull='" + urlFull + "'}";
    }
}
