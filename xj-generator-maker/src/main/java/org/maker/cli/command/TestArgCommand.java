package org.maker.cli.command;

import cn.hutool.core.bean.BeanUtil;
import freemarker.template.TemplateException;
import lombok.Data;
import org.maker.generator.file.FileGenerate;
import org.maker.model.DataModel;
import picocli.CommandLine;

import java.io.IOException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "generate", mixinStandardHelpOptions = true)
@Data
public class TestArgCommand implements Runnable {

    @CommandLine.Option(names = {"-l", "--loop"}, description = "是否循环", arity = "0..1", interactive = true, echo = true)
    private boolean loop;

    @CommandLine.Option(names = {"--needGit"}, arity = "0..1", description = "是否生成Git", interactive = true, echo = true)
    private boolean needGit;

    @CommandLine.ArgGroup(exclusive = true,heading = "核心模板%n")
    MainTemplate mainTemplate;

    @Override
    public void run() {
        System.out.println(loop);
        System.out.println(needGit);
        System.out.println(mainTemplate);
    }


    @Data
    public static class MainTemplate {

        @CommandLine.Option(names = {"-a", "--author"}, arity = "0..1", description = "作者注释", interactive = true, echo = true)
        private String author;

        @CommandLine.Option(names = {"-o", "--outputText"}, arity = "0..1", description = "输出信息", interactive = true, echo = true)
        private String outputText;
    }


/*    @Override
    public Integer call() throws Exception {
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        try {
            *//**
             * 调用静态文件生成
             *//*
            FileGenerate.doGenerate(dataModel);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }*/
    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(TestArgCommand.class);
       // args = new String[]{"--help"};
       args = new String[]{"-l","-a","-o","--needGit"};
        commandLine.execute(args);
    }


}


