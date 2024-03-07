package org.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.maker.meta.Meta;
import org.maker.meta.enums.FileGenerateTypeEnum;
import org.maker.meta.enums.FileTypeEnum;
import org.maker.template.enums.FileFilterRangeEnum;
import org.maker.template.enums.FileFilterRuleName;
import org.maker.template.model.FileFilterConfig;
import org.maker.template.model.TemplateMaker;
import org.maker.template.model.TemplateMakerConfig;
import org.maker.template.model.TemplateMakerModelConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.io.FileUtil.readUtf8String;

/**
 * 模板制作工具
 */

public class templateMaker {
    /**
     * @param newMeta
     * @param originProjectPath
     * @param templateMakerModelConfig
     * @param templateMakerConfig
     * @param id
     * @return
     */
    public static Long MakeTemplate(Meta newMeta, String originProjectPath, TemplateMakerModelConfig templateMakerModelConfig, TemplateMakerConfig templateMakerConfig, Long id) {
        if (id == null) {
            id = IdUtil.getSnowflakeNextId();
        }
        //复制目录
        String projectPath = System.getProperty("user.dir");
        //指定模板存放目录
        String templateDir = projectPath + File.separator + ".temp";
        //模板存放目录
        String templatePath = templateDir + File.separator + id;

        if (!FileUtil.exist(templateDir)) {
            FileUtil.mkdir(templateDir);
            FileUtil.copy(originProjectPath, templatePath, true);
        }
        //2.输入文件信息
        //文件存放位置
       /* String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");*/
        //originProjectPath参数如非首次制作已经存在，不需要再配置，所以修改该变量的获取方式
        //目录层级1，且必须读取目录而不是文件
        String sourceRootPath = FileUtil.loopFiles(new File(templatePath),1,null)
                .stream()
                .filter(File::isDirectory)
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getAbsolutePath()
                .replace('\\','/');

        List<Meta.FileConfig.FileInfo> newFileInfoList = makeFileTemplates(templateMakerModelConfig, templateMakerConfig, sourceRootPath);

        List<Meta.ModelConfig.ModelInfo> newModelInfoList = getModelInfoList(templateMakerModelConfig);


        //TODO 下面功能中Post相关代码在不同目录中，没有办法指定一个目录且写多个目录太过复杂->过滤机制
        /**
         * 控制是否生成帖子相关功能
         * 允许用户输入一个参数控制帖子相关代码生成Controller,Service，Data
         * 实现：
         * 1.过滤范围 根据文件名字或者文件内容
         * 2.过滤规则 contains(包含)、startWith(前缀匹配)、endWith(后缀匹配)、regex(正则)、equal(相等)
         */

        //生成配置文件meta.json
        String jsonOutPath = templatePath + File.separator + "meta.json";

        if (FileUtil.exist(jsonOutPath)) {
            Meta oldMeta = JSONUtil.toBean(readUtf8String(jsonOutPath), Meta.class);
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;
            //追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = newMeta.getModelConfig().getModels();
            modelInfoList.addAll(newModelInfoList);

            //去重相同的modelInfo和FileInfo
            oldMeta.getModelConfig().setModels(distinctModels(modelInfoList));
            oldMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(oldMeta), jsonOutPath);
        } else {
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);
            fileInfoList.addAll(newFileInfoList);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> models1 = new ArrayList<>();
            modelConfig.setModels(models1);
            models1.addAll(newModelInfoList);

            //输出json文件 bean转json字符串
            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), jsonOutPath);
        }
        return id;
    }

    private static List<Meta.ModelConfig.ModelInfo> getModelInfoList(TemplateMakerModelConfig templateMakerModelConfig) {
        //非空校验
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>();
        if (templateMakerModelConfig == null) return newModelInfoList;
        //处理模型信息
        List<TemplateMakerModelConfig.ModelInfoConfig> models = templateMakerModelConfig.getModels();
        if(CollUtil.isEmpty(models)) return newModelInfoList;

        //转换配置接受的ModelInfo对象
        List<Meta.ModelConfig.ModelInfo> inputModelInfolist = models.stream().map(modelInfoConfig -> {
            Meta.ModelConfig.ModelInfo modelInfo1 = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelInfoConfig, modelInfo1);
            return modelInfo1;
        }).collect(Collectors.toList());

        //模型组
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        if (modelGroupConfig != null) {
            // 复制变量
            Meta.ModelConfig.ModelInfo groupModelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelGroupConfig, groupModelInfo);

            // 模型全放到一个分组内
            groupModelInfo.setModels(inputModelInfolist);
            newModelInfoList.add(groupModelInfo);
        } else {
            // 不分组，添加所有的模型信息到列表
            newModelInfoList.addAll(inputModelInfolist);
        }
        return newModelInfoList;
    }

    private static List<Meta.FileConfig.FileInfo> makeFileTemplates(TemplateMakerModelConfig templateMakerModelConfig, TemplateMakerConfig templateMakerConfig,  String sourceRootPath) {
        //二.生成文件模板
        //TODO 此时可以支持单个文件或者目录来生成，应该增加输入多个文件的功能
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        if (templateMakerModelConfig == null) return newFileInfoList; //非空校验
        List<TemplateMakerConfig.FileInfoConfig> fileInfoConfigList = templateMakerConfig.getFiles();
        if(CollUtil.isEmpty(fileInfoConfigList)) return newFileInfoList;

        for (TemplateMakerConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {
            String inputFilePath = fileInfoConfig.getPath();
            //输入的是相对路径要改为绝对路径
            if (!inputFilePath.startsWith(sourceRootPath)) {
                inputFilePath = sourceRootPath + File.separator + inputFilePath;
            }

            //过滤 获取过滤后的文件列表（不会存在目录）
            List<File> fileList = FileFilter.doFilter(inputFilePath, fileInfoConfig.getFilterConfigList());

            //不处理已经生成的FTL模板文件
            fileList = fileList.stream()
                    .filter(file -> !file.getAbsolutePath().endsWith(".ftl"))
                    .collect(Collectors.toList());

            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(templateMakerModelConfig, file, sourceRootPath,fileInfoConfig);
                newFileInfoList.add(fileInfo);
            }
        }

        //如果是文件组
        TemplateMakerConfig.FileGroupConfig fileGroupConfig = templateMakerConfig.getFileGroupConfig();
        if (fileGroupConfig != null) {
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();
            String condition = fileGroupConfig.getCondition();

            //新增分组配置
            Meta.FileConfig.FileInfo groupFileInfo = new Meta.FileConfig.FileInfo();
            groupFileInfo.setType(FileTypeEnum.GROUP.getValue());
            groupFileInfo.setCondition(condition);
            groupFileInfo.setGroupKey(groupKey);
            groupFileInfo.setGroupName(groupName);
            //文件全部放到一个分组中
            groupFileInfo.setFiles(newFileInfoList);
            newFileInfoList = new ArrayList<>();
            //这里只有一个fileInfo了
            newFileInfoList.add(groupFileInfo);
        }
        return newFileInfoList;
    }

    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig, File inputFile, String sourceRootPath, TemplateMakerConfig.FileInfoConfig fileInfoConfig) {
        //要挖坑的文件的绝对路径
        String fileinputAbPath = inputFile.getAbsolutePath().replaceAll("\\\\", "/");
        String fileOutPutAbpath = fileinputAbPath + ".ftl";
        //要挖坑的文件（相对路径）用于生成配置文件
        String fileInputPath = fileinputAbPath.replace(sourceRootPath + "/", "");
        String fileOutPutPath = fileInputPath + ".ftl";
        //使用字符串替换，生成模板工具（绝对路径）
        String content;
        //判断输出目录是否存在，存在代表不是第一次修改
        boolean hasTemplateFile = FileUtil.exist(fileOutPutAbpath);
        if (hasTemplateFile) {
            //直接读取原输出路径
            content = readUtf8String(fileOutPutAbpath);
        } else {
            //TODO BUG，如果第一遍操作成功，后面传入id=null，报错找不到文件
            content = readUtf8String(fileinputAbPath);
        }

        // 支持多个模型：对同一个文件的内容，遍历模型进行多轮替换
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        String newFileContent = content;
        String replacement;

        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModels()) {
            // 不是分组
            if (modelGroupConfig == null) {
                replacement = String.format("${%s}", modelInfoConfig.getFieldName());
            } else {
                // 是分组
                String groupKey = modelGroupConfig.getGroupKey();
                // 注意挖坑要多一个层级
                replacement = String.format("${%s.%s}", groupKey, modelInfoConfig.getFieldName());
            }
            // 多次替换
            newFileContent = StrUtil.replace(newFileContent, modelInfoConfig.getReplaceText(), replacement);
        }

        // 文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        //绝对路径
        fileInfo.setInputPath(fileOutPutPath);
        fileInfo.setOutputPath(fileInputPath);
        fileInfo.setCondition(fileInfoConfig.getConditon());
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());//设置默认为动态生成

        boolean contentEquals = newFileContent.equals(content);
        // TODO BUG和原文件一致，没有挖坑，则为静态生成
        //之前不存在模板文件，并且修改后没有改变content，则为静态生成

        if (!hasTemplateFile) {
            if (contentEquals) {
                // 输入路径没有 FTL 后缀
                fileInfo.setInputPath(fileInputPath);
                fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
            } else {
                // 没有模板文件，需要挖坑，生成模板文件
                FileUtil.writeUtf8String(newFileContent, fileOutPutAbpath);
            }
        } else if (!contentEquals) {
            // 有模板文件，且增加了新坑，生成模板文件
            FileUtil.writeUtf8String(newFileContent, fileOutPutAbpath);
        }
        return fileInfo;
    }

    //TODO 现在已经具有对文件进行分组，并且通过给组设置condition的方式，支持用单个模型参数同时控制一组文件
    //TODO 但是还缺少快速生成文件组配置的能力

    /**
     * 现在已经可以单词制作多个文件了，而且根据用户习惯，同义词制作的可能属于同一组 ->可以不让用户手动分组
     * 方案一
     * 一个文件信息配置（FileInfoConfig）对应一次分组，如果传入的path是目录，则目录下的所有文件都为一组
     * 方案二
     * 一个完整的文件配置对应一次分组。即配置files列表中的所有文件都属于同组
     * @param fileInfo
     * @return
     */

    //TODO 模型分组

    /**
     *之前制作已经实现了模型分组的能力，现在也需要实习那
     * 思路：
     * 之前测试都是传入单个模型参数和要替换的字符串参数，但现在需要一次性输入多个模型参数，需要意义对应
     * 故需要额外的类封装这些参数
     */

    /**
     * 文件去重
     *
     * @param fileInfo
     * @return
     */

    /**
     * 模型去重
     *
     * @param modelInfoList
     * @return
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        // 策略：同分组内模型 merge，不同分组保留

        // 1. 有分组的，以组为单位划分
        Map<String, List<Meta.ModelConfig.ModelInfo>> groupKeyModelInfoListMap = modelInfoList
                .stream()
                .filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.ModelConfig.ModelInfo::getGroupKey)
                );


        // 2. 同组内的模型配置合并
        // 保存每个组对应的合并后的对象 map
        Map<String, Meta.ModelConfig.ModelInfo> groupKeyMergedModelInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.ModelConfig.ModelInfo>> entry : groupKeyModelInfoListMap.entrySet()) {
            List<Meta.ModelConfig.ModelInfo> tempModelInfoList = entry.getValue();
            List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(tempModelInfoList.stream()
                    .flatMap(modelInfo -> modelInfo.getModels().stream())
                    .collect(
                            Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                    ).values());

            // 使用新的 group 配置
            Meta.ModelConfig.ModelInfo newModelInfo = CollUtil.getLast(tempModelInfoList);
            newModelInfo.setModels(newModelInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedModelInfoMap.put(groupKey, newModelInfo);
        }

        // 3. 将模型分组添加到结果列表
        List<Meta.ModelConfig.ModelInfo> resultList = new ArrayList<>(groupKeyMergedModelInfoMap.values());

        // 4. 将未分组的模型添加到结果列表
        List<Meta.ModelConfig.ModelInfo> noGroupModelInfoList = modelInfoList.stream().filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupModelInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                ).values()));
        return resultList;
    }

    /**
     * 文件去重
     *
     * @param fileInfoList
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
        // 策略：同分组内文件 merge，不同分组保留

        // 1. 有分组的，以组为单位划分
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyFileInfoListMap = fileInfoList
                .stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey)
                );


        // 2. 同组内的文件配置合并
        // 保存每个组对应的合并后的对象 map
        Map<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : groupKeyFileInfoListMap.entrySet()) {
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())
                    .collect(
                            Collectors.toMap(Meta.FileConfig.FileInfo::getOutputPath, o -> o, (e, r) -> r)
                    ).values());

            // 使用新的 group 配置
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            newFileInfo.setFiles(newFileInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedFileInfoMap.put(groupKey, newFileInfo);
        }

        // 3. 将文件分组添加到结果列表
        List<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());

        // 4. 将未分组的文件添加到结果列表
        List<Meta.FileConfig.FileInfo> noGroupFileInfoList = fileInfoList.stream().filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupFileInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r)
                ).values()));
        return resultList;
    }


    //TODO BUG修复

    /**
     * 1.相同配置参数两个生成 dynamic->static
     * 2.错误处理了新生成的模板文件（ftl文件也会处理）
     * 3.文件输入和输出路径相反 根据源文件生成FTL模板文件，但是在代码生成器的元信息中，是根据FTL模板文件来生成目标文件的
     * 4.调整配置文件生成路径
     */


    public static Long MakeTemplate(TemplateMaker templateMaker) {
        Long id = templateMaker.getId();
        Meta meta = templateMaker.getMeta();
        String originProjectPath = templateMaker.getOriginProjectPath();
        TemplateMakerModelConfig templateMakerModelConfig = templateMaker.getModelConfig();
        TemplateMakerConfig templateMakerConfig = templateMaker.getFileConfig();

        return MakeTemplate(meta, originProjectPath, templateMakerModelConfig, templateMakerConfig, id);
    }

    public static void main(String[] args) {
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

        // 替换变量（首次）
//        String searchStr = "Sum: ";
        // 替换变量（第二次）
        String searchStr = "BaseResponse";

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

        long id = MakeTemplate(meta, originProjectPath, templateMakerModelConfig, templateMakerFileConfig, null);
        System.out.println(id);
    }
}