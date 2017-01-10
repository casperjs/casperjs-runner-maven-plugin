package com.github.casperjs.casperjsrunner.cmd;

public class NativeOptions {

    private boolean failFast;
    private boolean verbose;
    private String level;

    public NativeOptions(final boolean failFast, final boolean verbose, final String level) {
        this.failFast = failFast;
        this.verbose = verbose;
        this.level = level;
    }

    public boolean isFailFast() {
        return failFast;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public String getLevel() {
        return level;
    }
}
