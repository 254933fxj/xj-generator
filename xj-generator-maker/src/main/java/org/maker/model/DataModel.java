package org.maker.model;

import lombok.Data;
import org.maker.Main;

/**
 * 自定义模板
 * 1.在代码开头增加@author注解（增加代码）
 * 2.修改程序输出的信息提示
 * 3.将循环读取改为单次读取
 */
@Data
public class DataModel {
    /**
     * 作者
     */
    /**
     * 是否循环
     */
    public boolean loop;

    public boolean needGit;
    /**
     * 用于生成核心模板
     */
    public MainTemplate mainTemplate = new MainTemplate();
    @Data
    public static class MainTemplate {

        public String author = "fuxiaojie";
        /**
         * 输出内容
         */
        public String outPutText = "输出结果";
    }

}
