package com.bajingloncat.config;

import java.io.File;

public class AppConfig {

    private AppConfig() {}

    public static final String BASE_URL = "https://unsplash.com";
    public static final String NAPI_SEARCH_URL = BASE_URL + "/napi/search/photos";
    public static final String ANUBIS_PASS_URL = BASE_URL + "/.within.website/x/cmd/anubis/api/pass-challenge";

    public static final String USER_AGENT =
            "Mozilla/5.0 (X11; Linux x86_64; rv:152.0) Gecko/20100101 Firefox/152.0";

    public static final int PER_PAGE = 30;
    public static final int MAX_PAGES = 100;
    public static final int CONNECT_TIMEOUT_MS = 15000;
    public static final int READ_TIMEOUT_MS = 60000;
    public static final int DOWNLOAD_BUFFER_SIZE = 8192;

    public static File getDownloadDir() {
        File dir = new File("downloads");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
}
