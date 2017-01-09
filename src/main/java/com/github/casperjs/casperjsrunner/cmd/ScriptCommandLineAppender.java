package com.github.casperjs.casperjsrunner.cmd;

import org.apache.commons.exec.CommandLine;

public class ScriptCommandLineAppender implements CommandLineAppender {

    @Override
    public void append(final CommandLine cmdLine, final Parameters parameters) {
        if (parameters.getScriptFile() != null) {
            cmdLine.addArgument(parameters.getScriptFile().getAbsolutePath());
        } else {
            throw new MissingScriptFileException();
        }
    }

}
