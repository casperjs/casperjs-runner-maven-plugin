package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.LogUtils.getLogger;
import static com.github.casperjs.casperjsrunner.OSUtils.isWindows;
import static com.github.casperjs.casperjsrunner.toolchain.CasperjsToolchain.KEY_CASPERJS_TYPE;
import static java.lang.System.getProperty;
import static java.lang.System.getenv;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.toolchain.Toolchain;
import org.apache.maven.toolchain.ToolchainManager;

import java.io.File;

public class CasperJsRuntimeFinder {

    private CasperJsRuntimeFinder() {
        // only used as static
    }

    public static String findCasperRuntime(final ToolchainManager toolchainManager, final MavenSession session, final String casperExecPath) {
        String result = null;

        getLogger().debug("Finding casperjs runtime ...");

        getLogger().debug("Trying from toolchain");
        final Toolchain tc = toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session);
        if (tc != null) {
            getLogger().debug("Toolchain in casperjs-plugin: " + tc);
            if (casperExecPath != null) {
                getLogger().warn("Toolchains are ignored, 'casperRuntime' parameter is set to " + casperExecPath);
                result = casperExecPath;
            } else {
                getLogger().debug("Found from toolchain");
                result = tc.findTool(KEY_CASPERJS_TYPE);
            }
        }

        if (result == null) {
            getLogger().debug("No toolchain found, falling back to parameter");
            result = casperExecPath;
        }

        if (result == null) {
            String defaultCasperRuntime = "casperjs";
            if (isWindows()) {
                defaultCasperRuntime = findCasperInPath();
            }
            getLogger().debug("No parameter specified, falling back to default '" + defaultCasperRuntime + "'");
            result = defaultCasperRuntime;
        }

        return result;
    }

    private static String findCasperInPath() {
        String result = "casperjs";
        final String[] paths = getenv("PATH").split(getProperty("path.separator"));
        for (final String path : paths) {
            if (new File(path, "casperjs.exe").exists()) {
                result = "casperjs.exe";
            } else if (new File(path, "casperjs.bat").exists()) {
                result = "casperjs.bat";
            }
        }
        return result;
    }

}
