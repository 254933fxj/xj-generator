package org.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
//文件过滤规则枚举
@Getter
public enum FileFilterRuleName {

    CONTAINS("包含", "contains"),
    START_WITH("前缀匹配", "startsWith"),
    END_WITH("后缀匹配", "endsWith"),
    REGEX("正则", "regex"),
    EQUAL("相等", "equals");

    private final String text;
    private final String value;

    FileFilterRuleName(String text, String value) {
        this.text = text;
        this.value = value;
    }
    public static FileFilterRuleName getEnumByValue(String value){
        if(ObjectUtil.isEmpty(value)){
            return null;
        }
        for(FileFilterRuleName anEnum : FileFilterRuleName.values()){
            if(anEnum.value.equals(value)){
                return anEnum;
            }
        }
        return null;
    }
}
