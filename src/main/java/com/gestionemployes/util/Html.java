package com.gestionemployes.util;

public final class Html {

    private Html() {
    }

    public static String e(Object valeur) {
        if (valeur == null) {
            return "";
        }

        String s = valeur.toString();
        StringBuilder sb = new StringBuilder(s.length() + 16);

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '&' -> sb.append("&amp;");
                case '<' -> sb.append("&lt;");
                case '>' -> sb.append("&gt;");
                case '"' -> sb.append("&quot;");
                case '\'' -> sb.append("&#39;");
                default -> sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String ou(Object valeur) {
        String s = (valeur == null) ? "" : valeur.toString();
        return s.isEmpty() ? "—" : e(s);
    }
}
