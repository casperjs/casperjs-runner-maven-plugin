package com.github.casperjs.casperjsrunner.cmd;

public class ParametersFactory {

    private ParametersFactory() {
        // only used as static
    }

    public static Parameters buildParameters(final CasperJsRuntime runtime, final NativeOptions nativeOptions, final TestOptions testOptions,
            final Reports reports, final ScriptOptions scriptOptions) {
        return new Parameters(runtime, nativeOptions, testOptions, reports, scriptOptions);
    }
}
