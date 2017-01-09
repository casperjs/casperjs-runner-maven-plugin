package com.github.casperjs.casperjsrunner.cmd;

import org.apache.maven.artifact.versioning.ArtifactVersion;

import java.io.File;
import java.util.List;

public interface Parameters {

    String getCasperRuntime();

    ArtifactVersion getCasperJsVersion();

    boolean isFailFast();

    boolean isCasperjsVerbose();

    String getLogLevel();

    String getEngine();

    String getIncludes();

    List<String> getIncludesPatterns();

    File getIncludesDir();

    String getPre();

    String getPost();

    File getTestsDir();

    boolean isEnableXmlReports();

    File getReportsDir();

    String getScriptName();

    File getScriptFile();

    List<String> getArguments();

}
