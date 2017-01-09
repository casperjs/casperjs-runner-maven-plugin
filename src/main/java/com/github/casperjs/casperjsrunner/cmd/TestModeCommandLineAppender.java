package com.github.casperjs.casperjsrunner.cmd;

import org.apache.commons.exec.CommandLine;

public class TestModeCommandLineAppender implements CommandLineAppender {

    @Override
    public void append(final CommandLine cmdLine, final Parameters parameters) {
        cmdLine.addArgument("test");
    }

}
