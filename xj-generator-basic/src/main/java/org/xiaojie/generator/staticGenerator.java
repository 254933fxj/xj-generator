package org.xiaojie.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.LinkedTransferQueue;


/*
* 静态文件生成器
 */
public class staticGenerator {
    public static void main(String[] args) {
        //项目根目录
        String projectPath = System.getProperty("user.dir");
        //输入目录
        String inputPath = projectPath+ File.separator+"xj-generator-demo-projects"+File.separator+"acm-template";
        //输出目录
        String outputPath = projectPath;
        copyFileByRecursive(inputPath,outputPath);
    }

    /**
     *拷贝文件（实现 Hutool，会将输入目录完整拷贝到输出目录下）
     * @param srcPath  输入路径
     * @param  desPath 输出路径
     */
    public static void copyFilesByHutool(String srcPath,String desPath){
        FileUtil.copy(srcPath,desPath,false);
    }

    /**
     *递归拷贝文件（递归实现，会将输入目录完全拷贝到输出目录下）
     * @param inputPath
     * @param outputPath
     */
    public static void copyFileByRecursive(String inputPath,String outputPath){
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        try{
            copyFileByRecursive(inputFile,outputFile);
        }catch (Exception e){
            System.out.println("文件复制失败");
            e.printStackTrace();
        }
    }

    /**
     * 核心思路：先创建目录，然后递归遍历目录中的文件，依次复制
     * @param inputFile
     * @param outputFile
     * @throws IOException
     */
    private static void copyFileByRecursive(File inputFile,File outputFile)throws IOException {
        //区分是文件还是目录
        if(inputFile.isDirectory()){
            System.out.println(inputFile.getName());
            //TODO 输出目录可能存在
            File destOutputFile = new File(outputFile, inputFile.getName());
            //如果是目录，首先创建目标目录
            if(!destOutputFile.exists()){
                destOutputFile.mkdirs();
            }
            //获取目录下的所有文件和子目录
            File[] files = inputFile.listFiles();
            //无子文件直接结束
            if(ArrayUtil.isEmpty(files)){
                return ;
            }
            for(File file : files){
                //递归拷贝下一层
                copyFileByRecursive(file,destOutputFile);
            }
        }else{
            //是文件，直接复制到目标目录下
            Path desPath = outputFile.toPath().resolve(inputFile.getName());
            Files.copy(inputFile.toPath(),desPath, StandardCopyOption.REPLACE_EXISTING);

        }
    }

}

