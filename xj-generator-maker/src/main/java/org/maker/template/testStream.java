package org.maker.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class testStream {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
     static class Person{
        String name;
        Integer age;
    }
    public static void main(String[] args) {
        /**
         * 测试stream流去重
         */
        List<Person> list = new ArrayList<>();
        list.add(new Person("fxj",12));
        list.add(new Person("fxj",19));
        list.add(new Person("f",12));
        List<Person> list1 = new ArrayList<>(
                list.stream().collect(Collectors.toMap(Person::getName, o -> o, (e, r) -> r)).values()
        );
        for (int i = 0; i < list1.size(); i++) {
            System.out.println(list1.get(i).toString());
        }
    }
}
