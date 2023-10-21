package org.example;

import java.io.IOException;

public class Main {
    static private String query, numberPage, orientation = null, color = null, order = null, directory;
    static private int amount;
    static private GetDataResource getDataResource = new GetDataResource();

    public static void main(String[] args) {
        if (args.length >= 4 && (args[0].equals("-n") || args[0].equals("--normal"))) {
            //args length greater than 4 and length 1 start on 0,1,2,3 (total 4 length)
            //array start with number value 0
            // (args[0].equals("-n") || args[0].equals("--normal")) this code wrapped by () for reduce ambiguity
            query = String.valueOf(args[1]);
            amount = Integer.valueOf(args[2]);
            numberPage = String.valueOf(args[3]);
            getDataResource.setApiUrl(query, amount, numberPage, orientation, color, order, 1);
            //paramFeatureUrl with value 1 = all
        } else if (args.length == 4 && (args[0].equals("-f") || args[0].equals("--free"))) {
            query = String.valueOf(args[1]);
            amount = Integer.valueOf(args[2]);
            numberPage = String.valueOf(args[3]);
            getDataResource.setApiUrl(query, amount, numberPage, orientation, color, order, 2);
            //paramFeatureUrl with value 2 = free
        } else if (args.length == 5 && (args[0].equals("-o") || args[0].equals("--orient"))) {
            query = String.valueOf(args[1]);
            amount = Integer.valueOf(args[2]);
            numberPage = String.valueOf(args[3]);
            orientation = String.valueOf(args[4]);
            getDataResource.setApiUrl(query, amount, numberPage, orientation, color, order, 3);
            //paramOrientation (Valid values: landscape, portrait, squarish)
        } else if (args.length == 5 && (args[0].equals("-c") || args[0].equals("--color"))) {
            query = String.valueOf(args[1]);
            amount = Integer.valueOf(args[2]);
            numberPage = String.valueOf(args[3]);
            color = String.valueOf(args[4]);
            getDataResource.setApiUrl(query, amount, numberPage, orientation, color, order, 4);
        } else if (args.length == 5 && (args[0].equals("-r") || args[0].equals("--order"))) {
            query = String.valueOf(args[1]);
            amount = Integer.valueOf(args[2]);
            numberPage = String.valueOf(args[3]);
            order = String.valueOf(args[4]);
            getDataResource.setApiUrl(query, amount, numberPage, orientation, color, order, 5);
        } else if (args.length == 6 && (args[0].equals("-a") || args[0].equals("--all"))) {
            query = String.valueOf(args[1]);
            amount = Integer.valueOf(args[2]);
            numberPage = String.valueOf(args[3]);
            color = String.valueOf(args[4]);
            order = String.valueOf(args[5]);
            getDataResource.setApiUrl(query, amount, numberPage, orientation, color, order, 6);
        } else if (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help"))) {
            System.out.println("bajing-loncat (version 1.0, revision 1)");
            System.out.println("Usage:\n" +
                    " balon [OPTIONS]...[VALUES]\t\n" +
                    "  -n, --normal ['query'] [amount] [numberPage]    Normal data fetch.\n" +
                    "  -f, --free   ['query'] [amount] [numberPage]     Free data fetch.\n" +
                    "  -o, --orient ['query'] [amount] [numberPage] [orientation]    Specific orientation data fetch.\n" +
                    "  -c, --color  ['query'] [amount] [numberPage] [color]    Specific color data fetch.\n" +
                    "  -r, --order  ['query'] [amount] [numberPage] [order]    Specific order data fetch.\n" +
                    "  -a, --all    ['query'] [amount] [numberPage] [color] [order]    Perfect criteria data fetch.\n" +
                    "  -h, --help    Display usage,options and help.\n");
        } else {
            System.out.println("balon: missing operand\n" +
                    "Try 'balon -h or --help' for more information.");
        }
    }
}