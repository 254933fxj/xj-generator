package org.maker.cli.command;

import cn.hutool.core.util.ReflectUtil;
import org.maker.model.DataModel;
import picocli.CommandLine;

import java.lang.reflect.Field;
@CommandLine.Command(name = "config",mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable{
    @Override
    public void run() {
        Field[] fields = ReflectUtil.getFields(DataModel.class);
        for(Field filed : fields){
            System.out.println("字段类型: " + filed.getType() );
            System.out.println("字段名称: " + filed.getName());
            System.out.println("----");
        }
    }
}
