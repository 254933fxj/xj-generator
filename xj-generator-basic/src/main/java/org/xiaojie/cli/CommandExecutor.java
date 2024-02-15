package org.xiaojie.cli;

import org.xiaojie.cli.command.ConfigCommand;
import org.xiaojie.cli.command.GenerateCommand;
import org.xiaojie.cli.command.ListCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

//可以理解为遥控器或者用户实际区控制设备的
@Command(name = "xj",mixinStandardHelpOptions = true )
public class CommandExecutor implements Runnable {

    private CommandLine commandLine;

    /**
     * 代码快初始化，节省开销
     */
    {
        commandLine = new CommandLine(this)
                .addSubcommand(new GenerateCommand())
                .addSubcommand(new ListCommand())
                .addSubcommand(new ConfigCommand());
    }

    @Override
    public void run() {
        //不输入子命令是，给出友好提示
        System.out.println("请输入具体命令，或者 --help查看命令提示");
    }

    /**
     * 执行命令  由picocli框架实现,主命令就是一个调用者，整整执行的是子命令
     * @param args
     * @return
     */
    public Integer doExecutor(String[] args){
        return commandLine.execute(args);
    }

}
