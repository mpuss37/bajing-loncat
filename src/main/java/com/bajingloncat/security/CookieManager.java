package com.bajingloncat.security;

import com.bajingloncat.config.AppConfig;
import com.bajingloncat.exception.CookieException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class CookieManager {

    private String getCookieFilePath() {
        return new File(".cookie_cache").getAbsolutePath();
    }

    public String getValidCookie() {
        String cookie = loadFromCache();
        if (cookie != null && !cookie.isEmpty()) {
            System.out.println("  ✅ Cookie loaded dari cache");
            return cookie;
        }

        System.out.println("  🔄 Cookie tidak ada/expired, refresh via Python...");
        return refreshCookie();
    }

    private String loadFromCache() {
        try {
            Path path = Path.of(getCookieFilePath());
            if (Files.exists(path)) {
                String cookie = Files.readString(path).trim();
                if (!cookie.isEmpty()) {
                    return cookie;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public String refreshCookie() {
        try {
            String cookiePath = getCookieFilePath();
            String pythonScript = buildPythonScript();

            ProcessBuilder pb = new ProcessBuilder("python3", "-c", pythonScript, cookiePath);
            pb.redirectErrorStream(true);
            Process proc = pb.start();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println("  [py] " + line);
                }
            }

            int exitCode = proc.waitFor();
            if (exitCode != 0) {
                throw new CookieException("Python process exited with code " + exitCode);
            }

            String cookie = loadFromCache();
            if (cookie == null || cookie.isEmpty()) {
                throw new CookieException("Cookie file kosong setelah refresh");
            }

            System.out.println("  ✅ Cookie refreshed!");
            return cookie;

        } catch (CookieException e) {
            throw e;
        } catch (Exception e) {
            throw new CookieException("Gagal refresh cookie: " + e.getMessage(), e);
        }
    }

    private String buildPythonScript() {
        return """
                import hashlib, json, re, requests, time, sys, os, sqlite3, shutil

                session = requests.Session()

                headers = {
                    "User-Agent": "Mozilla/5.0 (X11; Linux x86_64; rv:152.0) Gecko/20100101 Firefox/152.0",
                    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                    "Accept-Language": "en-US,en;q=0.9",
                    "Accept-Encoding": "gzip, deflate, br",
                }

                # Step 1: Load cookies dari Firefox
                print("Step 1: Load cookies dari Firefox...")
                firefox_profiles = [
                    os.path.expanduser("~/.mozilla/firefox/1q2xxu3r.default-release-1718206130886/cookies.sqlite"),
                    os.path.expanduser("~/.mozilla/firefox/hp9uVvUA.Profile 1/cookies.sqlite"),
                ]
                loaded = False
                for db_path in firefox_profiles:
                    if os.path.exists(db_path):
                        try:
                            tmp_db = "/tmp/cookies_read.db"
                            shutil.copy2(db_path, tmp_db)
                            conn = sqlite3.connect(tmp_db)
                            cursor = conn.cursor()
                            cursor.execute("SELECT name, value FROM moz_cookies WHERE host LIKE '%unsplash%'")
                            for name, value in cursor.fetchall():
                                if name in ("azk", "azk-ss", "un_sesh", "require_cookie_consent", "xp-prices-2026"):
                                    session.cookies.set(name, value, domain="unsplash.com")
                                    print(f"  Loaded {name}")
                            conn.close()
                            os.remove(tmp_db)
                            loaded = True
                            break
                        except Exception as e:
                            print(f"  Error: {e}")
                            continue

                if not loaded:
                    print("ERROR: Tidak bisa load cookies dari Firefox")
                    print("Buka unsplash.com di Firefox dulu!")
                    sys.exit(1)

                # Step 2: Test cookie langsung ke NAPI
                print("Step 2: Test cookie ke NAPI...")
                test_url = "https://unsplash.com/napi/search/photos?query=cat&per_page=1&page=1"
                r_test = session.get(test_url, headers={**headers, "Accept": "application/json"})
                print(f"  NAPI test: {r_test.status_code}")

                if r_test.status_code == 200:
                    print("  Cookie masih valid!")
                else:
                    # Cookie expired, coba solve Anubis
                    print("  Cookie expired, solve Anubis...")
                    r1 = session.get("https://unsplash.com/", headers=headers)
                    print(f"  Visit /: {r1.status_code}")

                    match = re.search(r'id="anubis_challenge".*?>(.*?)</script>', r1.text, re.DOTALL)
                    if not match:
                        print("  ERROR: Tidak ada challenge dan cookie tidak valid")
                        print("  Buka unsplash.com di Firefox dulu untuk refresh cookies!")
                        sys.exit(1)

                    cd = json.loads(match.group(1))
                    prefix = "0" * cd["rules"]["difficulty"]
                    start_time = time.time()
                    nonce = 0
                    while True:
                        h = hashlib.sha256((cd["challenge"]["randomData"] + str(nonce)).encode()).hexdigest()
                        if h.startswith(prefix):
                            break
                        nonce += 1
                    elapsed = int((time.time() - start_time) * 1000)
                    print(f"  Nonce: {nonce}")

                    submit_headers = headers.copy()
                    submit_headers["Referer"] = "https://unsplash.com/"

                    params = {
                        "id": cd["challenge"]["id"],
                        "response": h,
                        "nonce": str(nonce),
                        "redir": "/",
                        "elapsedTime": str(elapsed)
                    }
                    r2 = session.get("https://unsplash.com/.within.website/x/cmd/anubis/api/pass-challenge",
                                     params=params, headers=submit_headers)
                    print(f"  Submit: {r2.status_code}")

                    # Test lagi setelah solve
                    r_test2 = session.get(test_url, headers={**headers, "Accept": "application/json"})
                    print(f"  NAPI re-test: {r_test2.status_code}")

                if r_test2.status_code != 200:
                    print("  ERROR: Cookie masih tidak valid setelah Anubis solve")
                    print("")
                    print("  ============================================")
                    print("  Cookie Firefox sudah EXPIRED!")
                    print("  Harap lakukan langkah berikut:")
                    print("  1. Buka Firefox")
                    print("  2. Kunjungi https://unsplash.com")
                    print("  3. Tunggu halaman load sempurna")
                    print("  4. Tutup Firefox")
                    print("  5. Jalankan ulang program ini")
                    print("  ============================================")
                    sys.exit(1)

                # Step 3: Save
                cookie_parts = [f"{c.name}={c.value}" for c in session.cookies]
                full_cookie = "; ".join(cookie_parts)

                cookie_file = sys.argv[1]
                with open(cookie_file, "w") as f:
                    f.write(full_cookie)

                print(f"Cookie saved ({len(full_cookie)} chars)")
                """;
    }
}
