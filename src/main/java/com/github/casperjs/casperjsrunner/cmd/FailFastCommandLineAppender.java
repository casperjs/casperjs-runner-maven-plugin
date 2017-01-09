package com.github.casperjs.casperjsrunner.cmd;

import org.apache.commons.exec.CommandLine;

public class FailFastCommandLineAppender implements CommandLineAppender {

    @Override
    public void append(final CommandLine cmdLine, final Parameters parameters) {
        if (parameters.isFailFast()) {
            cmdLine.addArgument("--fail-fast");
        }
    }

}
