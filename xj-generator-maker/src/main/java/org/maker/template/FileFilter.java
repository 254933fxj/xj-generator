package org.maker.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import org.maker.template.enums.FileFilterRangeEnum;
import org.maker.template.enums.FileFilterRuleName;
import org.maker.template.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class FileFilter {
    /**
     * 单个文件过滤
     *
     * @param fileFilterConfigList 过滤规则
     * @param file                 单个文件
     * @return 是否保留
     */
    public static boolean doSingleFilter(List<FileFilterConfig> fileFilterConfigList, File file) {
        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);

        //所有过滤器校验结束的结果
        boolean result = true;

        if (CollUtil.isEmpty(fileFilterConfigList)) {
            return true;
        }

        for (FileFilterConfig fileFilterConfig : fileFilterConfigList) {
            String rule = fileFilterConfig.getRule();//过滤规则
            String value = fileFilterConfig.getValue();
            String range = fileFilterConfig.getRange();//过滤范围
            //过滤范围
            FileFilterRangeEnum fileFilterRangeEnumByValue = FileFilterRangeEnum.getFileFilterRangeEnumByValue(range);
            if (fileFilterRangeEnumByValue == null) continue;

            //要过滤的原内容（是文件名称还是文件内容）
            String content = fileName;
            switch (fileFilterRangeEnumByValue) {
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
            }

            FileFilterRuleName fileFilterRuleName = FileFilterRuleName.getEnumByValue(rule);
            if (fileFilterRuleName == null) continue;
            switch (fileFilterRuleName) {
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case START_WITH:
                    result = content.startsWith(value);
                    break;
                case END_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUAL:
                    result = content.equals(value);
                    break;
                default:
            }
        }
        //有一个不满足直接返回
        return result;
    }

    /**
     * 对某个文件或者目录进行过滤，返回列表元素
     * @param filePath
     * @param fileFilterConfigList
     * @return
     */
    public static List<File> doFilter(String filePath,List<FileFilterConfig> fileFilterConfigList){
        //根据路径获取所有文件
        List<File> fileList = FileUtil.loopFiles(filePath);
        return fileList.stream()
                .filter(file -> doSingleFilter(fileFilterConfigList,file))
                .collect(Collectors.toList());
    }
}
