package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.LogUtils.getLogger;
import static java.util.Arrays.asList;

import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultScriptsFinder implements ScriptsFinder {

    private final File baseDir;

    private final String specific;

    private final List<String> includes;
    private final List<String> excludes;

    public DefaultScriptsFinder(final File baseDir, final String specific, final List<String> includes, final List<String> excludes) {
        if (includes == null || includes.isEmpty()) {
            throw new IllegalArgumentException("Include patterns to search must be defined !");
        }
        if (excludes == null) {
            throw new IllegalArgumentException("Excludes patterns must not be null !");
        }
        this.baseDir = baseDir;
        this.specific = specific;
        this.includes = includes;
        this.excludes = excludes;
    }

    @Override
    public Collection<String> findScripts() {
        getLogger().info("Looking for scripts in " + baseDir + "...");

        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setCaseSensitive(false);
        scanner.setBasedir(baseDir);
        if (specific != null && !specific.isEmpty()) {
            final List<String> temp = new ArrayList<String>();
            if (specific.endsWith(".js") || specific.endsWith(".coffee")) {
                temp.add("**/" + specific);
            } else {
                temp.add("**/" + specific + ".js");
                temp.add("**/" + specific + ".coffee");
            }
            scanner.setIncludes(temp.toArray(new String[temp.size()]));
        } else {
            scanner.setIncludes(includes.toArray(new String[includes.size()]));
            scanner.setExcludes(excludes.toArray(new String[excludes.size()]));
        }
        scanner.scan();

        final List<String> result = asList(scanner.getIncludedFiles());
        if (result.isEmpty()) {
            getLogger().warn("No files found in directory " + baseDir + " matching criterias");
        }

        return result;
    }

}
