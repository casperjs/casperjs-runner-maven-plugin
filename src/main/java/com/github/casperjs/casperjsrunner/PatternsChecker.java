package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.LogUtils.getLogger;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public class PatternsChecker {

    private static final String[] DEFAULT_JS_PATTERNS = { "**/Test*.js", "**/*Test.js", "**/*TestCase.js" };

    private static final String[] DEFAULT_CS_PATTERNS = { "**/Test*.coffee", "**/*Test.coffee", "**/*TestCase.coffee" };

    private PatternsChecker() {
        // only used as static
    }

    public static List<String> checkPatterns(final List<String> patterns, final boolean includeJS, final boolean includeCS) {
        if (!includeJS) {
            getLogger().info("JavaScript files ignored");
        }
        if (!includeCS) {
            getLogger().info("CoffeeScript files ignored");
        }

        if (patterns == null || patterns.isEmpty()) {
            return computeDefaultPatterns(includeJS, includeCS);
        } else {
            return computePatterns(patterns, includeJS, includeCS);
        }
    }

    private static List<String> computeDefaultPatterns(final boolean includeJS, final boolean includeCS) {
        final List<String> result = new ArrayList<String>();

        if (includeCS) {
            result.addAll(asList(DEFAULT_CS_PATTERNS));
        }
        if (includeJS) {
            result.addAll(asList(DEFAULT_JS_PATTERNS));
        }

        return result;
    }

    private static List<String> computePatterns(final List<String> patterns, final boolean includeJS, final boolean includeCS) {
        final List<String> result = new ArrayList<String>();

        for (final String pattern : patterns) {
            if (pattern.endsWith(".coffee")) {
                if (includeCS) {
                    result.add(pattern);
                }
            } else if (pattern.endsWith(".js")) {
                if (includeJS) {
                    result.add(pattern);
                }
            } else {
                result.add(pattern);
            }
        }

        return result;
    }
}
