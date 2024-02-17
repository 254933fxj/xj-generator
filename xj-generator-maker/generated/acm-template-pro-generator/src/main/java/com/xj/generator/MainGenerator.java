package com.xj.generator;

import com.xj.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
* 核心生成器
*/
public class MainGenerator {

/**
* 生成
*
* @param model 数据模型
* @throws TemplateException
* @throws IOException
*/
public static void doGenerate(Object model) throws TemplateException, IOException {
String inputRootPath = "D:/code/xj-generator/xj-generator-demo-projects/xj-generator-demo-projects-pro";
String outputRootPath = "generated";

String inputPath;
String outputPath;

        inputPath = new File(inputRootPath, "src/com/yupi/acm/MainTemplate.java.ftl").getAbsolutePath();

        outputPath = new File(outputRootPath, "src/com/yupi/acm/MainTemplate.java").getAbsolutePath();
        System.out.println("输出路径:"+outputPath);
        DynamicGenerator.doGenerate(inputPath, outputPath, model);

        inputPath = new File(inputRootPath, ".gitignore").getAbsolutePath();

        outputPath = new File(outputRootPath, ".gitignore").getAbsolutePath();
        System.out.println("输出路径:"+outputPath);
        DynamicGenerator.doGenerate(inputPath, outputPath, model);

        inputPath = new File(inputRootPath, "README.md").getAbsolutePath();

        outputPath = new File(outputRootPath, "README.md").getAbsolutePath();
        System.out.println("输出路径:"+outputPath);
        DynamicGenerator.doGenerate(inputPath, outputPath, model);
}
}