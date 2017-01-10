package com.github.casperjs.casperjsrunner.cmd;

import java.io.File;
import java.util.List;

public class TestOptions {

    private String engine;
    private String includes;
    private List<String> includesPatterns;
    private File includesDir;
    private String pre;
    private String post;
    private File testsDir;

    public TestOptions(final String engine, final String includes, final List<String> includesPatterns, final File includesDir, final String pre,
            final String post, final File testsDir) {
        this.engine = engine;
        this.includes = includes;
        this.includesPatterns = includesPatterns;
        this.includesDir = includesDir;
        this.pre = pre;
        this.post = post;
        this.testsDir = testsDir;
    }

    public String getEngine() {
        return engine;
    }

    public String getIncludes() {
        return includes;
    }

    public List<String> getIncludesPatterns() {
        return includesPatterns;
    }

    public File getIncludesDir() {
        return includesDir;
    }

    public String getPre() {
        return pre;
    }

    public String getPost() {
        return post;
    }

    public File getTestsDir() {
        return testsDir;
    }
}
