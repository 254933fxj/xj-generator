package org.maker.template.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileFilterConfig {
    /**
     * 过滤规则 有5种
     */
    private String rule;
    /**
     * 过滤值
     */
    private String value;
    /**
     *过滤范围
     */
    private String range;
}
