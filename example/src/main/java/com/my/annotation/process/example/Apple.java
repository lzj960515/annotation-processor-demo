package com.my.annotation.process.example;

import com.my.annotaion.process.annotation.Factory;

/**
 * 苹果
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
@Factory(id = "apple", type = Fruit.class)
public class Apple implements Fruit {

    @Override
    public Float getPrice() {
        return 3F;
    }
}
