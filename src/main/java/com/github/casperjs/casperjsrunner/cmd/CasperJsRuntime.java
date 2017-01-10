package com.github.casperjs.casperjsrunner.cmd;

import org.apache.maven.artifact.versioning.ArtifactVersion;

public class CasperJsRuntime {

    private String exe;
    private ArtifactVersion version;

    public CasperJsRuntime(final String exe, final ArtifactVersion version) {
        this.exe = exe;
        this.version = version;
    }

    public String getExe() {
        return exe;
    }

    public ArtifactVersion getVersion() {
        return version;
    }
}
