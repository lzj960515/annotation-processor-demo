package com.my.annotation.process.example;

/**
 * 测试
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
public class FruitTest {

    public static void main(String[] args) {
        Fruit apple = FruitFactory.create("apple");
        System.out.println(apple.getPrice());
    }
}
