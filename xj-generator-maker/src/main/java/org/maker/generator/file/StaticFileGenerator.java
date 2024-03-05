package org.maker.generator.file;

import cn.hutool.core.io.FileUtil;


/*
* 静态文件生成器
 */
public class StaticFileGenerator {

    /**
     *拷贝文件（实现 Hutool，会将输入目录完整拷贝到输出目录下）
     * @param srcPath  输入路径
     * @param  desPath 输出路径
     */
    public static void copyFilesByHutool(String srcPath,String desPath){
        FileUtil.copy(srcPath,desPath,false);
    }

}

