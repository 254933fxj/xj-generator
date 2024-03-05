package org.maker.template.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.maker.meta.Meta;

import java.util.List;
@Data
public class TemplateMakerModelConfig {
    private List<ModelInfoConfig> models;
    private ModelGroupConfig modelGroupConfig;

    @Data
    public static class ModelGroupConfig {
        private String groupKey;
        private String groupName;
        private String condition;
    }

    @Data
    @NoArgsConstructor
    public static class ModelInfoConfig {
        /**
         * 要替换的名称
         */
        private String fieldName;
        private String type;
        private String description;
        private Object defaultValue;
        private String abbr;
        /**
         * 替换的内容
         */
        private String replaceText;
    }
}
