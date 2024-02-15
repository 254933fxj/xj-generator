package org.xiaojie;

import org.xiaojie.cli.CommandExecutor;


public class Main {
    public static void main(String[] args) {
        CommandExecutor commandExecutor = new CommandExecutor();
        //args = new String[]{"generate","-l","-a","-o"};
        //args = new String[]{"config"};
        //args = new String[]{"list"};
        commandExecutor.doExecutor(args);
    }
}