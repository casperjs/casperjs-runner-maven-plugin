package com.github.casperjs.casperjsrunner.cmd;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;

public class LogLevelCommandLineAppender implements CommandLineAppender {

    @Override
    public void append(final CommandLine cmdLine, final Parameters parameters) {
        if (StringUtils.isNotBlank(parameters.getLogLevel())) {
            cmdLine.addArgument("--log-level=" + parameters.getLogLevel());
        }
    }

}
