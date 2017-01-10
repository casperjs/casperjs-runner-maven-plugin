package com.github.casperjs.casperjsrunner.cmd;

import java.io.File;
import java.util.List;

public class ScriptOptions {

    private String scriptName;
    private File file;
    private List<String> arguments;

    public ScriptOptions(final String scriptName, final File file, final List<String> arguments) {
        this.scriptName = scriptName;
        this.file = file;
        this.arguments = arguments;
    }

    public String getScriptName() {
        return scriptName;
    }

    public File getFile() {
        return file;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
