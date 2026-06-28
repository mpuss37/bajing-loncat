package com.bajingloncat;

import com.bajingloncat.cli.CommandLineRunner;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1 || args[0].equals("-h") || args[0].equals("--help")) {
            CommandLineRunner.printUsage();
            return;
        }

        try {
            int jumlah = Integer.parseInt(args[0]);
            String query = (args.length > 1) ? args[1] : null;
            new CommandLineRunner().run(jumlah, query);
        } catch (NumberFormatException e) {
            System.out.println("❌ Jumlah harus angka, contoh: java -jar bajing-loncat.jar 10 cat");
        }
    }
}
