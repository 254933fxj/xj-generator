package org.maker.meta.enums;

public enum FileTypeEnum {

    DIR("目录", "dir"),

    GROUP("文件组", "group"),
    FILE("文件", "file");



    private final String text;

    private final String value;

    FileTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}