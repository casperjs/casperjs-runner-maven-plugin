package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.toolchain.CasperjsToolchain.KEY_CASPERJS_TYPE;
import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.toolchain.Toolchain;
import org.apache.maven.toolchain.ToolchainManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.mockito.Mock;

import java.io.File;

public class CasperJsRuntimeFinderTest {

    @Rule
    public final EnvironmentVariables envVars = new EnvironmentVariables();

    @Mock
    private ToolchainManager toolchainManager;

    @Mock
    private Toolchain toolchain;

    @Mock
    private MavenSession session;

    @Before
    public void initializeMocks() {
        initMocks(this);

        LogUtils.setLog(mock(Log.class), false);
    }

    @Test
    public void testFindCasperJsRuntimeFromToolchain() {
        when(toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session)).thenReturn(toolchain);
        when(toolchain.findTool(KEY_CASPERJS_TYPE)).thenReturn("casperjs runtime from toolchain");

        assertEquals("casperjs runtime from toolchain", CasperJsRuntimeFinder.findCasperRuntime(toolchainManager, session, null));
    }

    @Test
    public void testFindCasperJsRuntimeFromToolchainButOverriden() {
        when(toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session)).thenReturn(toolchain);
        when(toolchain.findTool(KEY_CASPERJS_TYPE)).thenReturn("casperjs runtime from toolchain");

        assertEquals("casperjs runtime from parameter",
                CasperJsRuntimeFinder.findCasperRuntime(toolchainManager, session, "casperjs runtime from parameter"));
    }

    @Test
    public void testFindCasperJsRuntimeFromParameter() {
        when(toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session)).thenReturn(null);

        assertEquals("casperjs runtime from parameter",
                CasperJsRuntimeFinder.findCasperRuntime(toolchainManager, session, "casperjs runtime from parameter"));
    }

    @Test
    public void testFindCasperJsRuntimeFromDefault() {
        when(toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session)).thenReturn(null);
        setProperty("os.name", "Linux");

        assertEquals("casperjs", CasperJsRuntimeFinder.findCasperRuntime(toolchainManager, session, null));
    }

    @Test
    public void testFindCasperJsRuntimeFromDefaultOnWindows() throws Exception {
        File casper = null;
        try {
            when(toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session)).thenReturn(null);
            setProperty("os.name", "Windows");
            envVars.set("PATH", getProperty("java.io.tmpdir"));
            casper = new File(getProperty("java.io.tmpdir"), "casperjs.exe");
            casper.createNewFile();

            assertEquals("casperjs.exe", CasperJsRuntimeFinder.findCasperRuntime(toolchainManager, session, null));
        } finally {
            if (casper != null) {
                casper.delete();
            }
        }
    }

    @Test
    public void testFindCasperJsRuntimeFromDefaultOnWindowsForOlderCasperjs() throws Exception {
        File casper = null;
        try {
            when(toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session)).thenReturn(null);
            setProperty("os.name", "Windows");
            envVars.set("PATH", getProperty("java.io.tmpdir"));
            casper = new File(getProperty("java.io.tmpdir"), "casperjs.bat");
            casper.createNewFile();

            assertEquals("casperjs.bat", CasperJsRuntimeFinder.findCasperRuntime(toolchainManager, session, null));
        } finally {
            if (casper != null) {
                casper.delete();
            }
        }
    }
}
