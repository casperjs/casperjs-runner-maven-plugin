package com.github.casperjs.casperjsrunner.cmd;

public class MissingScriptFileException extends RuntimeException {

    private static final long serialVersionUID = 3220982558909380790L;

    public MissingScriptFileException() {
        super("Script to run was not found");
    }
}
