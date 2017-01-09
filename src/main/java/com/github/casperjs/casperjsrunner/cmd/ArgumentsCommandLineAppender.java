package com.github.casperjs.casperjsrunner.cmd;

import static com.github.casperjs.casperjsrunner.cmd.ArgQuoter.quote;

import org.apache.commons.exec.CommandLine;

public class ArgumentsCommandLineAppender implements CommandLineAppender {

    @Override
    public void append(final CommandLine cmdLine, final Parameters parameters) {
        if (parameters.getArguments() != null && !parameters.getArguments().isEmpty()) {
            for (final String argument : parameters.getArguments()) {
                cmdLine.addArgument(quote(argument), false);
            }
        }
    }

}
