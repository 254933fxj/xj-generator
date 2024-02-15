package org.xiaojie.cli.command;

import cn.hutool.core.bean.BeanUtil;
import freemarker.template.TemplateException;
import lombok.Data;
import org.xiaojie.generator.mainGenerate;
import org.xiaojie.model.MainTemplate;
import picocli.CommandLine;

import java.io.IOException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "generate",mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable {

    /**
     * 作者
     */
    @CommandLine.Option(names = {"-a","--author"},description = "作者名称",arity = "0..1",interactive = true,echo = true)
    private String author = "fuxiaojie";
    /**
     * 输出内容
     */
    @CommandLine.Option(names = {"-o","--output"},description = "输出结果",arity = "0..1",interactive = true,echo = true)
    private String outPutText = "输出结果";
    /**
     * 是否循环
     */
    @CommandLine.Option(names = {"-l","--loop"},description = "是否循环",arity = "0..1",interactive = true,echo = true)
    private boolean loop;


    @Override
    public Integer call() throws Exception {
        MainTemplate mainTemplate = new MainTemplate();
        BeanUtil.copyProperties(this,mainTemplate);
        try {
            mainGenerate.doGenerate(mainTemplate);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
