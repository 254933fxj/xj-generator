package org.xiaojie.model;

import lombok.Data;

/**
 * 自定义模板
 * 1.在代码开头增加@author注解（增加代码）
 * 2.修改程序输出的信息提示
 * 3.将循环读取改为单次读取
 */
@Data
public class MainTemplate {
    /**
     * 作者
     */
    private String author = "fuxiaojie";
    /**
     * 输出内容
     */
    private String outPutText = "输出结果";
    /**
     * 是否循环
     */
    private boolean loop;
}
