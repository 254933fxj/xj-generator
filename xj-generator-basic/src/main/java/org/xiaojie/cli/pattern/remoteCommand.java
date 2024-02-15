package org.xiaojie.cli.pattern;

import java.util.List;

public class remoteCommand {
    private Command command;

    //获取历史记录，那就直接在遥控器上记录
    private List<Command> historyCommand;

    public remoteCommand(Command command){
        this.command = command;
    }
    public void run(){
        historyCommand.add(command);
        command.execute();
    }
}
