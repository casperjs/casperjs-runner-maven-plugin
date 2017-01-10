package com.github.casperjs.casperjsrunner.cmd;

import java.io.File;

public class Reports {

    private boolean enable;
    private File directory;

    public Reports(final boolean enable, final File directory) {
        this.enable = enable;
        this.directory = directory;
    }

    public boolean isEnable() {
        return enable;
    }

    public File getDirectory() {
        return directory;
    }
}
