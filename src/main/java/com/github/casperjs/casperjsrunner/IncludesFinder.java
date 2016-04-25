package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.LogUtils.getLogger;
import static java.util.Arrays.asList;

import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.List;

public class IncludesFinder {

    private final File baseDir;

    private final List<String> patterns;

    public IncludesFinder(final File baseDir, final List<String> patterns) {
        if (patterns == null || patterns.isEmpty()) {
            throw new IllegalArgumentException("Patterns to search must be defined !");
        }
        this.baseDir = baseDir;
        this.patterns = patterns;
    }

    public List<String> findIncludes() {
        getLogger().info("Looking for includes in " + baseDir + "...");

        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setCaseSensitive(false);
        scanner.setBasedir(baseDir);
        scanner.setIncludes(patterns.toArray(new String[patterns.size()]));
        scanner.scan();

        return asList(scanner.getIncludedFiles());
    }

}
