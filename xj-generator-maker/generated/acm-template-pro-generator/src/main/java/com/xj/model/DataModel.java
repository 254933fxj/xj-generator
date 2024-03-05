package com.xj.model;

import lombok.Data;


/**
* 数据模型
*/
@Data
public class DataModel {

            /**
             * 是否生成Git
             */
        public boolean needGit;

            /**
             * 是否生成循环
             */
        public boolean loop;

        /**
        * 核心模板
        */
        public MainTemplate mainTemplate = new MainTemplate();

        /**
        * 用户生成核心配置模型
        */
        @Data
        public static class MainTemplate {
                /**
                 * 作者注释
                 */
            public String author;
                /**
                 * 输出信息
                 */
            public String outputText;
        }

}