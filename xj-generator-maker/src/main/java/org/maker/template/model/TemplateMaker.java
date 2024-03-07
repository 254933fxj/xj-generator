package org.maker.template.model;

import lombok.Data;
import org.maker.meta.Meta;

import java.util.List;

/**
 * 模板制作工具
 */
@Data
public class TemplateMaker {
    private Long id;
    private Meta meta = new Meta();
    private String originProjectPath;
    private TemplateMakerConfig fileConfig = new TemplateMakerConfig();
    private TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig();
}
