package org.xiaojie.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.xiaojie.model.MainTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class dynamicGenerator {
    public static void main(String[] args) throws IOException, TemplateException {
        String projectPath = System.getProperty("user.dir")+File.separator+"xj-generator-basic";
        String inputPath = projectPath+File.separator+"src/main/resources/templates/MainTemplate.java.ftl";
        String outPutPath = projectPath+File.separator+"MainTemplate.java";

        //创建数据模型
        MainTemplate mainTemplate = new MainTemplate();
        mainTemplate.setAuthor("符晓杰");
        mainTemplate.setOutPutText("输出结果");
        mainTemplate.setLoop(true);

        doGenerate(inputPath,outPutPath,mainTemplate);
    }

    static void doGenerate(String inputPath,String outPath,Object model) throws IOException, TemplateException {
        //new出Configuration对象，参数为FreeMarker版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        //指定模板文件所在路径
        File templatesDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templatesDir);
        //设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");
        configuration.setNumberFormat("0.######");
        //创建模板对象，加载指定模板引擎
        String templateName = new File(inputPath).getName();
        Template template = configuration.getTemplate(templateName);
        //创建数据模型
        Writer writer = new FileWriter(outPath);
        template.process(model,writer);
        writer.close();
    }
}
