package com.bajingloncat.service;

import com.bajingloncat.config.AppConfig;
import com.bajingloncat.model.Photo;
import com.bajingloncat.repository.UnsplashRepository;
import com.bajingloncat.util.FileUtils;

import java.io.File;
import java.util.List;

public class PhotoService {

    private final UnsplashRepository repository;

    public PhotoService() {
        this.repository = new UnsplashRepository();
    }

    public void downloadPhotos(int jumlah, String query) {
        System.out.println();

        if (query == null || query.isEmpty()) {
            System.out.println("🔍 Mencari " + jumlah + " gambar random");
        } else {
            System.out.println("🔍 Mencari " + jumlah + " gambar \"" + query);
        }

        System.out.println();

        List<Photo> photos;

        if (query == null || query.isEmpty()) {
            photos = repository.getRandomPhotos(jumlah);
        } else {
            photos = repository.searchPhotos(query, jumlah);
        }

        if (photos.isEmpty()) {
            System.out.println("❌ Tidak ditemukan gambar");
            return;
        }

        System.out.println();
        System.out.println("✅ Ditemukan " + photos.size() + " gambar");
        System.out.println("📁 Download ke: " + AppConfig.getDownloadDir().getAbsolutePath());
        System.out.println();

        int success = 0;
        int failed = 0;

        for (int i = 0; i < photos.size(); i++) {
            Photo photo = photos.get(i);
            String prefix = (query != null && !query.isEmpty()) ? query.replaceAll("\\s+", "_") : "photo";
            String filename = prefix + "_" + String.format("%03d", i + 1) + ".jpg";
            File outputFile = new File(AppConfig.getDownloadDir(), filename);

            System.out.print("📥 [" + (i + 1) + "/" + photos.size() + "] " + filename + " ");

            try {
                FileUtils.downloadImage(photo.getUrlFull(), outputFile);
                System.out.println("✅");
                success++;
            } catch (Exception e) {
                System.out.println("❌ " + e.getMessage());
                failed++;
            }
        }

        System.out.println();
        System.out.println("═══════════════════════════════════════");
        System.out.println("✅ Selesai!");
        System.out.println("   Berhasil: " + success);
        if (failed > 0) {
            System.out.println("   Gagal: " + failed);
        }
        System.out.println("   Folder: " + AppConfig.getDownloadDir().getAbsolutePath());
        System.out.println("═══════════════════════════════════════");
        System.out.println();
    }
}
