# bajing-loncat 🐿️

Unsplash Image Downloader - Download gambar random dari Unsplash secara otomatis.

## Instalasi

### Prerequisites

- Java 17 atau lebih tinggi
- Python 3 dengan library `requests`
- Firefox (untuk cookie)

### Install Java

```bash
# Arch Linux
pacman -S jdk-openjdk

# Debian/Ubuntu
apt install openjdk-17-jdk

# Windows
# Download dari: https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe
```

### Install Python requests

```bash
pip3 install requests
```

### Build

```bash
git clone https://github.com/mpuss37/bajing-loncat.git
cd bajing-loncat
./gradlew jar
```

File JAR akan ada di `build/libs/bajing-loncat.jar`

## Cara Pakai

```bash
# Download 10 gambar random
java -jar build/libs/bajing-loncat.jar 10

# Download 50 gambar random
java -jar build/libs/bajing-loncat.jar 50

# Tampilkan bantuan
java -jar build/libs/bajing-loncat.jar -h
```

### Output

```
🔍 Mencari 10 gambar random dari Unsplash...

  📡 Fetching dari Unsplash...
    Page 1: 30 foto (total: 30)
    Page 2: 30 foto (total: 60)

✅ Ditemukan 10 gambar
📁 Download ke: /home/user/bajing-loncat/downloads

📥 [1/10] photo_001.jpg ✅
📥 [2/10] photo_002.jpg ✅
📥 [3/10] photo_003.jpg ✅
...

═══════════════════════════════════════
✅ Selesai!
   Berhasil: 10
   Folder: /home/user/bajing-loncat/downloads
═══════════════════════════════════════
```

### Struktur Folder

```
bajing-loncat/
├── downloads/
│   ├── photo_001.jpg
│   ├── photo_002.jpg
│   └── ...
├── build/
│   └── libs/
│       └── bajing-loncat.jar
└── src/
    └── main/
        └── java/
            └── com/
                └── bajingloncat/
                    ├── Main.java
                    ├── cli/
                    ├── config/
                    ├── dto/
                    ├── exception/
                    ├── model/
                    ├── repository/
                    ├── security/
                    ├── service/
                    └── util/
```

## Catatan

- Firefox harus pernah visit unsplash.com (untuk cookie)
- Gambar didownload dalam format JPG full resolution
- Cookie di-cache di `/tmp/unsplash_cookie.txt`
- Cookie otomatis di-refresh jika expired

## Lisensi

MIT
