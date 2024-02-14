package org.xiaojie.generator;

import freemarker.template.TemplateException;
import org.xiaojie.model.MainTemplate;

import java.io.File;
import java.io.IOException;

import static org.xiaojie.generator.dynamicGenerator.doGenerate;
import static org.xiaojie.generator.staticGenerator.copyFileByRecursive;

public class mainGenerate {
    public static void main(String[] args) throws TemplateException, IOException {
        //项目根目录
        String projectPath = System.getProperty("user.dir");
        //输入目录
        String inputPath = projectPath+ File.separator+"xj-generator-demo-projects"+File.separator+"acm-template";

        //1.静态生成文件
        copyFileByRecursive(inputPath,projectPath);

        String DynamicProjectPath = System.getProperty("user.dir")+File.separator+"xj-generator-basic";
        String dynamicInputPath = DynamicProjectPath+File.separator+"src/main/resources/templates/MainTemplate.java.ftl";
        String dynamicOutPutPath = projectPath+File.separator+"acm-template/src/com/yupi/acm/MainTemplate.java";

        MainTemplate mainTemplate = new MainTemplate();
        mainTemplate.setAuthor("符晓杰");
        mainTemplate.setOutPutText("输出结果");
        mainTemplate.setLoop(true);

        // 2.动态替换目录

        doGenerate(dynamicInputPath,dynamicOutPutPath,mainTemplate);

    }
}
