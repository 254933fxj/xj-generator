package org.maker;

import org.maker.cli.CommandExecutor;


public class Main {
    public static void main(String[] args) {
        CommandExecutor commandExecutor = new CommandExecutor();
        args = new String[]{"generate","-l","-a","-o","--needGit"};
        //args = new String[]{"generate","--needGit=false"};
        //args = new String[]{"config"};
        //args = new String[]{"list"};
        commandExecutor.doExecutor(args);
    }
}