package org.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.maker.meta.Meta;
import org.maker.template.enums.FileFilterRangeEnum;
import org.maker.template.enums.FileFilterRuleName;
import org.maker.template.model.FileFilterConfig;
import org.maker.template.model.TemplateMaker;
import org.maker.template.model.TemplateMakerConfig;
import org.maker.template.model.TemplateMakerModelConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.maker.template.templateMaker.MakeTemplate;

public class templateMakerTest {

    @Test
    public void makeTemplate() {
        //1.指定配置文件基本信息
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM 示例模板生成器");

        String projectPath = System.getProperty("user.dir");
        //2.指定生成模板相对根路径
        String originProjectPath = new File(projectPath).getParent() + File.separator + "xj-generator-demo-projects/springboot-init";
        //3.指定要生成文件的相对路径
        String inputFilePath1 = "src/main/java/com/yupi/springbootinit/common";
        String inputFilePath2 = "src/main/resources/application.yml";

        // 4.模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();

        // - 模型组配置
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
        modelGroupConfig.setGroupKey("mysql");
        modelGroupConfig.setGroupName("数据库配置");
        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);

        // - 模型配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");

        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig2.setFieldName("username");
        modelInfoConfig2.setType("String");
        modelInfoConfig2.setDefaultValue("root");
        modelInfoConfig2.setReplaceText("root");

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1, modelInfoConfig2);
        templateMakerModelConfig.setModels(modelInfoConfigList);

        // 文件过滤
        TemplateMakerConfig templateMakerFileConfig = new TemplateMakerConfig();
        TemplateMakerConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath1);
        List<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
                .range(FileFilterRangeEnum.FILE_NAME.getValue())
                .rule(FileFilterRuleName.CONTAINS.getValue())
                .value("Base")
                .build();
        fileFilterConfigList.add(fileFilterConfig);
        fileInfoConfig1.setFilterConfigList(fileFilterConfigList);

        TemplateMakerConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerConfig.FileInfoConfig();
        fileInfoConfig2.setPath(inputFilePath2);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1, fileInfoConfig2));

        // 分组配置
        TemplateMakerConfig.FileGroupConfig fileGroupConfig = new TemplateMakerConfig.FileGroupConfig();
        fileGroupConfig.setCondition("outputText");
        fileGroupConfig.setGroupKey("test");
        fileGroupConfig.setGroupName("测试分组");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);

        long id = MakeTemplate(meta, originProjectPath, templateMakerModelConfig, templateMakerFileConfig, 1764830932200292352L);
        System.out.println(id);
    }

    @Test
    public void makeSpringBootTemplate() {
        String rootPath = "example/springboot-init/";
        String configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker.json");
        TemplateMaker templateMakerConfig = JSONUtil.toBean(configStr, TemplateMaker.class);
        Long id = MakeTemplate(templateMakerConfig);
        System.out.println(id);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker1.json");
        templateMakerConfig = JSON.parseObject(configStr,TemplateMaker.class);
        MakeTemplate(templateMakerConfig);
        //帖子相关
        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker2.json");
        templateMakerConfig = JSON.parseObject(configStr,TemplateMaker.class);
        MakeTemplate(templateMakerConfig);
        //是否跨域
        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker3.json");
        templateMakerConfig = JSON.parseObject(configStr,TemplateMaker.class);
        MakeTemplate(templateMakerConfig);
        //是否开启接口文档功能
        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker4.json");
        templateMakerConfig = JSON.parseObject(configStr,TemplateMaker.class);
        MakeTemplate(templateMakerConfig);
        //接口文档配置
        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker5.json");
        templateMakerConfig = JSON.parseObject(configStr,TemplateMaker.class);
        MakeTemplate(templateMakerConfig);
        //自定义Mysql相关配置
        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker6.json");
        templateMakerConfig = JSON.parseObject(configStr,TemplateMaker.class);
        MakeTemplate(templateMakerConfig);
        //redis替换
        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker7.json");
        templateMakerConfig = JSON.parseObject(configStr,TemplateMaker.class);
        MakeTemplate(templateMakerConfig);
        //开启ElasticSearcch
        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker8.json");
        templateMakerConfig = JSON.parseObject(configStr,TemplateMaker.class);
        MakeTemplate(templateMakerConfig);
    }

}