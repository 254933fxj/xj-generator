package org.maker.generator;


import freemarker.template.TemplateException;
import org.maker.generator.file.DynamicFileGenerator;
import org.maker.generator.file.StaticFileGenerator;
import org.maker.model.DataModel;

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
    public static void doGenerate(DataModel model) throws TemplateException, IOException {
        String inputRootPath = "./source/xj-generator-demo-projects-pro";
        String outputRootPath = "generated";


        String inputPath;
        String outputPath;

        inputPath = new File(inputRootPath, "src/com/yupi/acm/MainTemplate.java.ftl").getAbsolutePath();
        outputPath = new File(outputRootPath, "src/com/yupi/acm/MainTemplate.java").getAbsolutePath();
        DynamicFileGenerator.doGenerate(inputPath, outputPath, model);
        /**
         * todo 一个模型参数对应某个文件是否生成
         * 1.在doGenerate方法中注入DataModel模型，方便后面获取属性
         * 2.通过if语句判断是否生成.gitinore文件
         */

            inputPath = new File(inputRootPath, ".gitignore").getAbsolutePath();
            outputPath = new File(outputRootPath, ".gitignore").getAbsolutePath();
            StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);


        inputPath = new File(inputRootPath, "README.md").getAbsolutePath();
        outputPath = new File(outputRootPath, "README.md").getAbsolutePath();
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
    }

}