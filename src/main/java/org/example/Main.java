package org.example;

import java.io.IOException;

public class Main {
    static private String query, amount, numberPage;

    static private GetDataResource getDataResource = new GetDataResource();
    public static void main(String[] args) throws IOException {
        if (args.length == 4 && args[0].equals("-g") || args[0].equals("--get")) {
            query = String.valueOf(args[1]);
            amount = String.valueOf(args[2]);
            numberPage = String.valueOf(args[3]);
            getDataResource.setApiUrl("elephant","1","1");
        }
    }
}