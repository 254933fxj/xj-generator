package org.xiaojie.cli.example;


import picocli.CommandLine;

import java.util.concurrent.Callable;

class Login implements Callable<Integer> {
    @CommandLine.Option(names = {"-u","--user"},description = "User Name")
    String user;
    @CommandLine.Option(names = {"-p","--password"},description = "Passparase",arity = "0..1",interactive = true)
    String password;
    @CommandLine.Option(names = {"-cp","--cpassword"},description = "CheckPassword",arity = "0..1",interactive = true)
    String checkPassword;

    public Integer call() throws Exception {
        System.out.println("password = "+password);
        return 0;
    }

    public static void main(String[] args) {
        new CommandLine(new Login()).execute("-u","fuxiaojie","-p","123456","-cp","d");
    }

}