package org.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

public class MetaManager {
    private  static volatile Meta meta;//保证可见性
    //DataModel.java.ftl b c 双检锁单例模式 vs 饿汉式单例模式
    public static Meta getMeta(){
        if(meta==null){
            // DataModel.java.ftl b c
            synchronized (MetaManager.class){
                //防止一次进入
                if(meta==null) {
                    meta = initMeta();
                }
            }
        }
        return meta;
    }
    private static Meta initMeta(){
        //Hutool工具类将json对象转换为字符串
        String metaStr = ResourceUtil.readUtf8Str("meta.json");
        //Hutool工具类将字符串转换为Bean
        Meta newMeta = JSONUtil.toBean(metaStr, Meta.class);
        //TODO 检验配置文件 处理默认值
        return newMeta;
    }
}
