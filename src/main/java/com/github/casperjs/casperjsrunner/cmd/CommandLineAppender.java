package com.github.casperjs.casperjsrunner.cmd;

import org.apache.commons.exec.CommandLine;

public interface CommandLineAppender {

    void append(CommandLine cmdLine, Parameters parameters);

}
