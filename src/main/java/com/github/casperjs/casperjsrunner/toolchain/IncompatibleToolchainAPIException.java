package com.github.casperjs.casperjsrunner.toolchain;

public class IncompatibleToolchainAPIException extends RuntimeException {

    private static final long serialVersionUID = 5880556566753900117L;

    public IncompatibleToolchainAPIException(final Exception ex) {
        super("Incompatible toolchain API", ex);
    }
}
