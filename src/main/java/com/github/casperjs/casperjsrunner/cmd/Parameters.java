package com.github.casperjs.casperjsrunner.cmd;

import org.apache.maven.artifact.versioning.ArtifactVersion;

import java.io.File;
import java.util.List;

public class Parameters {

    private CasperJsRuntime runtime;
    private NativeOptions nativeOptions;
    private TestOptions testOptions;
    private Reports reports;
    private ScriptOptions scriptOptions;

    public Parameters(final CasperJsRuntime runtime, final NativeOptions nativeOptions, final TestOptions testOptions, final Reports reports,
            final ScriptOptions scriptOptions) {
        this.runtime = runtime;
        this.nativeOptions = nativeOptions;
        this.testOptions = testOptions;
        this.reports = reports;
        this.scriptOptions = scriptOptions;
    }

    public String getCasperRuntime() {
        return runtime.getExe();
    }

    public ArtifactVersion getCasperJsVersion() {
        return runtime.getVersion();
    }

    public boolean isFailFast() {
        return nativeOptions.isFailFast();
    }

    public boolean isCasperjsVerbose() {
        return nativeOptions.isVerbose();
    }

    public String getLogLevel() {
        return nativeOptions.getLevel();
    }

    public String getEngine() {
        return testOptions.getEngine();
    }

    public String getIncludes() {
        return testOptions.getIncludes();
    }

    public List<String> getIncludesPatterns() {
        return testOptions.getIncludesPatterns();
    }

    public File getIncludesDir() {
        return testOptions.getIncludesDir();
    }

    public String getPre() {
        return testOptions.getPre();
    }

    public String getPost() {
        return testOptions.getPost();
    }

    public File getTestsDir() {
        return testOptions.getTestsDir();
    }

    public boolean isEnableXmlReports() {
        return reports.isEnable();
    }

    public File getReportsDir() {
        return reports.getDirectory();
    }

    public String getScriptName() {
        return scriptOptions.getScriptName();
    }

    public File getScriptFile() {
        return scriptOptions.getFile();
    }

    public List<String> getArguments() {
        return scriptOptions.getArguments();
    }

}
