package org.maker.template.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.maker.meta.Meta;

import java.util.List;

/**
 * 封装所有和文件相关的配置
 */
@Data
public class TemplateMakerConfig {
    private List<FileInfoConfig> files;
    private FileGroupConfig fileGroupConfig;

    @Data
    @NoArgsConstructor
    public static class FileInfoConfig {
        /**
         * 文件路径
         */
        private String path;
        /**
         * 文件过滤规则
         */
        private List<FileFilterConfig> filterConfigList;
    }

    @Data
    public static class FileGroupConfig {
        /**
         *
         */
        private String groupKey;
        /**
         * 组名
         */
        private String groupName;
        /**
         *
         */
        private String condition;
    }
}
