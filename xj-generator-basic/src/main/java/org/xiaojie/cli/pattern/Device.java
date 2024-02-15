package org.xiaojie.cli.pattern;

public class Device {
    private String name;
    public Device(String name){
        this.name = name;
    }
    public void turnon(){
        System.out.println(name+"打开电视");
    }
    public void turnoff(){
        System.out.println(name+"关闭电视");
    }
}
