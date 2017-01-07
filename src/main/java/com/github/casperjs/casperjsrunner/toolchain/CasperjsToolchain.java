package com.github.casperjs.casperjsrunner.toolchain;

import org.apache.maven.toolchain.DefaultToolchain;
import org.apache.maven.toolchain.model.ToolchainModel;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;

/**
 * Based on {@code org.apache.maven.toolchain.java.DefaultJavaToolChain}.
 */
public class CasperjsToolchain extends DefaultToolchain {

    public static final String KEY_CASPERJS_TYPE = "casperjs";

    public static final String KEY_CASPERJS_EXECUTABLE = "casperjsExecutable";

    private String casperjsExecutable;

    protected CasperjsToolchain(final ToolchainModel model, final Logger logger, final String casperjsExecutable) {
        super(model, KEY_CASPERJS_TYPE, logger);
        this.casperjsExecutable = casperjsExecutable;
    }

    @Override
    public String findTool(final String toolName) {
        if (KEY_CASPERJS_TYPE.equals(toolName)) {
            final File casperjs = new File(FileUtils.normalize(casperjsExecutable));
            if (casperjs.exists()) {
                return casperjs.getAbsolutePath();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "CASPERJS[" + casperjsExecutable + "]";
    }
}
