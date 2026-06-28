package com.bajingloncat.cli;

import com.bajingloncat.service.PhotoService;

public class CommandLineRunner {

    public void run(int jumlah, String query) {
        if (jumlah <= 0) {
            System.out.println("❌ Jumlah harus lebih dari 0");
            return;
        }

        if (jumlah > 100) {
            System.out.println("⚠️  Maksimal 100 gambar per run");
            jumlah = 100;
        }

        PhotoService service = new PhotoService();

        if (query == null || query.isEmpty()) {
            service.downloadPhotos(jumlah, null);
        } else {
            service.downloadPhotos(jumlah, query);
        }
    }

    public static void printUsage() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           🐿️  bajing-loncat                  ║");
        System.out.println("║     Unsplash Image Downloader               ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Cara pakai:");
        System.out.println("  java -jar bajing-loncat.jar <jumlah> [query]");
        System.out.println();
        System.out.println("Contoh:");
        System.out.println("  java -jar bajing-loncat.jar 10           # Random images");
        System.out.println("  java -jar bajing-loncat.jar 10 cat       # Gambar kucing");
        System.out.println("  java -jar bajing-loncat.jar 20 mountain  # Gambar gunung");
        System.out.println("  java -jar bajing-loncat.jar 5 nature     # Gambar alam");
        System.out.println();
        System.out.println("Opsi:");
        System.out.println("  -h, --help    Tampilkan bantuan ini");
        System.out.println();
        System.out.println("Catatan:");
        System.out.println("  - Gambar akan didownload ke folder ./downloads/");
        System.out.println("  - Format file: photo_001.jpg, photo_002.jpg, dst.");
        System.out.println("  - Firefox harus pernah visit unsplash.com");
        System.out.println();
    }
}
