package com.github.casperjs.casperjsrunner.cmd;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.junit.Test;

import java.io.File;

public class ParametersFactoryTest {

    @Test
    public void testBuildParameters() {
        final ArtifactVersion casperJsVersion = mock(ArtifactVersion.class);
        final File includesDir = new File("includesDir");
        final File testsDir = new File("testsDir");
        final File reportsDir = new File("reportsDir");
        final File f = new File("f");

        final Parameters params = ParametersFactory.buildParameters(new CasperJsRuntime("runtime", casperJsVersion),
                new NativeOptions(true, false, "logLevel"),
                new TestOptions("engine", "includes", newArrayList("includesPatterns"), includesDir, "pre", "post", testsDir),
                new Reports(true, reportsDir), new ScriptOptions("scriptName", f, newArrayList("arguments")));

        assertEquals("runtime", params.getCasperRuntime());
        assertEquals(casperJsVersion, params.getCasperJsVersion());
        assertTrue(params.isFailFast());
        assertFalse(params.isCasperjsVerbose());
        assertEquals("logLevel", params.getLogLevel());
        assertEquals("engine", params.getEngine());
        assertEquals("includes", params.getIncludes());
        assertEquals(newArrayList("includesPatterns"), params.getIncludesPatterns());
        assertEquals(includesDir, params.getIncludesDir());
        assertEquals("pre", params.getPre());
        assertEquals("post", params.getPost());
        assertEquals(testsDir, params.getTestsDir());
        assertTrue(params.isEnableXmlReports());
        assertEquals(reportsDir, params.getReportsDir());
        assertEquals("scriptName", params.getScriptName());
        assertEquals(f, params.getScriptFile());
        assertEquals(newArrayList("arguments"), params.getArguments());
    }
}
