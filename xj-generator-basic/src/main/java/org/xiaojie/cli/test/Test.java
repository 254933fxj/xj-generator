package org.xiaojie.cli.test;

import java.util.function.Function;

public interface Test {
    void eat(String name);
}

 interface Test1 {
    Person eat(String name);
}

class Person{
    String name;
    int age = 22;
    public Person(){

    }

    public Person(String name) {
        this.name = name;
        //this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

class user{
    public static void main(String[] args) {
        Test t = (name) -> System.out.println(name+"欢迎使用lambda表达式");
        t.eat("符晓杰");

        Test1 test = Person::new;
        Person p2 = test.eat("张三");
        System.out.println("姓名:" + p2.getName());
        System.out.println("年龄:" + p2.getAge());

        Function<String ,Person> f = Person::new;
        Person p = f.apply("符晓杰");
        System.out.println("姓名:" + p.getName());
        System.out.println("年龄:" + p.getAge());

    }
}