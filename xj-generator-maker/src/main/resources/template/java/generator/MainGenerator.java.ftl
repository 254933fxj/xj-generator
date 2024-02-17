package ${basePackage}.generator;

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
String inputRootPath = "${fileConfig.inputRootPath}";
String outputRootPath = "${fileConfig.outputRootPath}";

String inputPath;
String outputPath;
<#list fileConfig.files as fileInfo>

    <#if fileInfo.type == "static">
        inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
        outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
        System.out.println("输出路径:"+outputPath);
        StaticGenerator.copyFilesByHutool(inputPath, outputPath);
    <#else >
        inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();

        outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
        System.out.println("输出路径:"+outputPath);
        DynamicGenerator.doGenerate(inputPath, outputPath, model);
    </#if>
</#list>
}
}