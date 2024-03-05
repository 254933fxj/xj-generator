package com.xj.cli.command;
import cn.hutool.core.bean.BeanUtil;
import com.xj.generator.MainGenerator;
import com.xj.model.DataModel;
import lombok.Data;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;


@Command(name = "generate", description = "生成代码", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {

        @Option(names = {"--needGit"}, arity = "0..1", description = "是否生成Git", interactive = true, echo = true)
        private boolean needGit;

        @Option(names = {"-l", "--loop"}, arity = "0..1", description = "是否生成循环", interactive = true, echo = true)
        private boolean loop;

            /**
            * 核心模板
            */
            static DataModel.MainTemplate mainTemplate = new DataModel.MainTemplate();

            @Command(name = "mainTemplate")
            @Data
            public static class MainTemplateCommand implements Runnable {
            @Option(names = {"-a", "--author"}, arity = "0..1", description = "作者注释", interactive = true, echo = true)
            private String author;
            @Option(names = {"-o", "--outputText"}, arity = "0..1", description = "输出信息", interactive = true, echo = true)
            private String outputText;

            @Override
            public void run() {
                mainTemplate.author = author;
                mainTemplate.outputText = outputText;
            }
            }

    public Integer call() throws Exception {
                if (loop) {
                System.out.println("输入核心模板配置：");
                CommandLine commandLine = new CommandLine(MainTemplateCommand.class);
                commandLine.execute("--author", "--outputText");
                }
    DataModel dataModel = new DataModel();
    BeanUtil.copyProperties(this, dataModel);
            dataModel.mainTemplate = mainTemplate;
    MainGenerator.doGenerate(dataModel);
    return 0;
    }
    }