package org.xiaojie.generator;

import freemarker.template.TemplateException;
import org.xiaojie.model.MainTemplate;

import java.io.File;
import java.io.IOException;

import static org.xiaojie.generator.staticGenerator.copyFileByRecursive;

public class mainGenerate {
    public static void doGenerate(Object model) throws TemplateException, IOException {

        String inputRootPath = "D:\\code\\xj-generator\\xj-generator-demo-projects\\xj-generator-demo-projects-pro";
        String outputRootPath = "D:\\code\\xj-generator";

        String inputPath;
        String outputPath;

        inputPath = new File(inputRootPath,"src/com/yupi/acm/MainTemplate.java.ftl").getAbsolutePath();
        outputPath = new File(outputRootPath,"acm-template/src/com/yupi/acm/MainTemplate.java").getAbsolutePath();
        DynamicFileGenerator.doGenerate(inputPath,outputPath,model);

        inputPath = new File(inputRootPath,".gitignore").getAbsolutePath();
        outputPath = new File(outputRootPath,".gitignore").getAbsolutePath();
        staticGenerator.copyFilesByHutool(inputPath,outputPath);

        inputPath = new File(inputRootPath,"README.md").getAbsolutePath();
        outputPath = new File(outputRootPath,"README.md").getAbsolutePath();
        staticGenerator.copyFilesByHutool(inputPath,outputPath);

    }

    public static void main(String[] args) throws TemplateException, IOException {
        MainTemplate mainTemplate = new MainTemplate();
        mainTemplate.setLoop(false);
        mainTemplate.setAuthor("符晓杰");
        mainTemplate.setOutPutText("sum = ");
        doGenerate(mainTemplate);
    }
}
