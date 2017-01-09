package com.github.casperjs.casperjsrunner.cmd;

import org.apache.maven.artifact.versioning.ArtifactVersion;

import java.io.File;
import java.util.List;

public class ParametersFactory {

    private ParametersFactory() {
        // only used as static
    }

    public static Parameters buildParameters(final String casperRuntime, final ArtifactVersion casperJsVersion, final boolean failFast,
            final boolean casperjsVerbose, final String logLevel, final String engine, final String includes, final List<String> includesPatterns,
            final File includesDir, final String pre, final String post, final File testsDir, final boolean enableXmlReports, final File reportsDir,
            final String scriptName, final File f, final List<String> arguments) {
        return new Parameters() {
            @Override
            public String getCasperRuntime() {
                return casperRuntime;
            }

            @Override
            public ArtifactVersion getCasperJsVersion() {
                return casperJsVersion;
            }

            @Override
            public boolean isFailFast() {
                return failFast;
            }

            @Override
            public boolean isCasperjsVerbose() {
                return casperjsVerbose;
            }

            @Override
            public String getLogLevel() {
                return logLevel;
            }

            @Override
            public String getEngine() {
                return engine;
            }

            @Override
            public String getIncludes() {
                return includes;
            }

            @Override
            public List<String> getIncludesPatterns() {
                return includesPatterns;
            }

            @Override
            public File getIncludesDir() {
                return includesDir;
            }

            @Override
            public String getPre() {
                return pre;
            }

            @Override
            public String getPost() {
                return post;
            }

            @Override
            public File getTestsDir() {
                return testsDir;
            }

            @Override
            public boolean isEnableXmlReports() {
                return enableXmlReports;
            }

            @Override
            public File getReportsDir() {
                return reportsDir;
            }

            @Override
            public String getScriptName() {
                return scriptName;
            }

            @Override
            public File getScriptFile() {
                return f;
            }

            @Override
            public List<String> getArguments() {
                return arguments;
            }
        };
    }
}
