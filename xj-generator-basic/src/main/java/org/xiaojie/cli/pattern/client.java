package org.xiaojie.cli.pattern;

public class client {
    public static void main(String[] args) {
        /**
         * 接收者：Device
         * 传话者：Command
         * 发送者：client
         * 面试问题：命令模式有哪些结构组成
         */
        //创建设备
        Device device1 = new Device("平板");
        Device device2 = new Device("电视");
        //创建按钮，然后绑定设备，执行操作
        Command command1 = new TurnOffCommand(device1);
        Command command2 = new TurnOnCommand(device2);
        //运行
        command1.execute();
        command2.execute();
    }
}
