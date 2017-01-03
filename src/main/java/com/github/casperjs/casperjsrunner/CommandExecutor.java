package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.LogUtils.getLogger;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class CommandExecutor {

    public static int executeCommand(final CommandLine line, final Map<String, String> environmentVariables, final boolean verbose,
            final File logFile) {
        getLogger().debug("Execute CasperJS command [" + line + "], with env: " + environmentVariables);
        FileOutputStream fos = null;
        try {
            final DefaultExecutor executor = new DefaultExecutor();
            if (logFile != null) {
                if (verbose) {
                    getLogger().info("Will duplicate output to: " + logFile.getAbsolutePath());
                }
                fos = new FileOutputStream(logFile);
                executor.setStreamHandler(new PumpStreamHandler(new CloneOutputStream(System.out, fos)));
            }
            executor.setExitValues(new int[] { 0, 1 });
            return executor.execute(line, environmentVariables);
        } catch (final IOException e) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (final IOException ioex) {
                    // ignore
                }
            }
            if (verbose) {
                getLogger().error("Could not run CasperJS command", e);
            }
            return -1;
        }
    }

}
