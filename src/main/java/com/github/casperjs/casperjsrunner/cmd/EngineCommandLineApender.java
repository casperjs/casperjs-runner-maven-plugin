package com.github.casperjs.casperjsrunner.cmd;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;

public class EngineCommandLineApender implements CommandLineAppender {

    @Override
    public void append(final CommandLine cmdLine, final Parameters parameters) {
        if (StringUtils.isNotBlank(parameters.getEngine())) {
            cmdLine.addArgument("--engine=" + parameters.getEngine());
        }
    }

}
