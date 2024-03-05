package org.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;


@Getter
/**
 * 文件过滤范围枚举
 */
public enum FileFilterRangeEnum {
    FILE_NAME("文件名称","fileName"),
    FILE_CONTENT("文件内容","fileContent");

    private final String context;
    private final String value;

    FileFilterRangeEnum(String context, String value) {
        this.context = context;
        this.value = value;
    }

    /**
     * 根据value值获取枚举
     * @param value
     * @return
     */
    public static FileFilterRangeEnum getFileFilterRangeEnumByValue(String value){
        if(ObjectUtil.isEmpty(value)){
            return null;
        }
        for(FileFilterRangeEnum anEnum : FileFilterRangeEnum.values()){
            if(anEnum.value.equals(value)){
                return anEnum;
            }
        }
        return null;
    }
}
