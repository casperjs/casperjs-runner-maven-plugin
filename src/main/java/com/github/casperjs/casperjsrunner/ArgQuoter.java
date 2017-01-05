package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.OSUtils.isWindows;

public class ArgQuoter {

    private ArgQuoter() {
        // only used as static
    }

    public static String quote(final String s) {
        if (!isWindows() || !needQuoting(s)) {
            return s;
        }

        final String quotesReplaced = s.replaceAll("([\\\\]*)\"", "$1$1\\\\\"");
        final String spacessReplaced = quotesReplaced.replaceAll("([\\\\]*)\\z", "$1$1");
        return "'" + spacessReplaced + "'";
    }

    private static boolean needQuoting(final String s) {
        if (s == null) {
            return false;
        }

        final int len = s.length();

        if (len == 0) {
            // empty string have to be quoted
            return true;
        }

        for (int i = 0; i < len; i++) {
            final char c = s.charAt(i);
            if (c == ' ' || c == '\t' || c == '\\' || c == '"') {
                return true;
            }
        }

        return false;
    }
}
