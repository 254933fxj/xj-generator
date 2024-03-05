package org.maker.generator.file;

import freemarker.template.TemplateException;
import org.maker.meta.Meta;
import org.maker.meta.MetaManager;
import org.maker.model.DataModel;

import java.io.File;
import java.io.IOException;

/**
 * 核心生成器
 */
public class FileGenerate {

    /**
     * 生成MainTeplate模板
     *
     * @param model 数据模型
     * @throws TemplateException
     * @throws IOException
     */
    public static void doGenerate(Object model) throws TemplateException, IOException {
        String projectPath = System.getProperty("user.dir");
        // 整个项目的根路径
        File parentFile = new File(projectPath).getParentFile();
        // 输入路径
        String inputPath = new File(parentFile, "xj-generator-demo-projects/acm-template").getAbsolutePath();
        String outputPath = projectPath;
        System.out.println("输出路径:"+outputPath);
        // 生成静态文件
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
        // 生成动态文件
        String inputDynamicFilePath = projectPath + File.separator + "src/main/resources/template/MainTemplate.java.ftl";
        String outputDynamicFilePath = outputPath + File.separator + "acm-template/src/com/yupi/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerate(inputDynamicFilePath, outputDynamicFilePath, model);
    }

    public static void main(String[] args) throws TemplateException, IOException {
      /*  DataModel dataModel = new DataModel();
        dataModel.setOutPutText("sum = ");
        dataModel.setLoop(true);
        dataModel.setAuthor("符晓杰");
        doGenerate(dataModel);*/
    }
}