package com.yupi.springbootinit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ���ࣨ��Ŀ������ڣ�
 */
// todo ���迪�� Redis�����Ƴ� exclude �е�����
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.yupi.springbootinit.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MainApplication {
    //_cFRbRxx5m4-v-h6Bnzy

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
