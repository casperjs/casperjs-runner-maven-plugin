package com.github.casperjs.casperjsrunner.cmd;

import static com.google.common.collect.Lists.newArrayList;

import org.apache.commons.exec.CommandLine;

import java.util.List;

public class CommandLineComputer {

    private static final CommandLineComputer INSTANCE = new CommandLineComputer();

    private final List<CommandLineAppender> appenders;

    CommandLineComputer() {
        this(newArrayList(new VerboseCommandLineAppender(), new LogLevelCommandLineAppender(), new EngineCommandLineApender(),
                new TestModeCommandLineAppender(), new IncludesCommandLineAppender(), new PreCommandLineAppender(), new PostCommandLineAppender(),
                new XUnitCommandLineAppender(), new FailFastCommandLineAppender(), new ScriptCommandLineAppender(),
                new ArgumentsCommandLineAppender()));
    }

    CommandLineComputer(final List<CommandLineAppender> appenders) {
        this.appenders = appenders;
    }

    CommandLine compute(final Parameters parameters) {
        final CommandLine cmdLine = new CommandLine(parameters.getCasperRuntime());

        for (final CommandLineAppender appender : appenders) {
            appender.append(cmdLine, parameters);
        }

        return cmdLine;
    }

    public static CommandLine computeCmdLine(final Parameters parameters) {
        return INSTANCE.compute(parameters);
    }

}
