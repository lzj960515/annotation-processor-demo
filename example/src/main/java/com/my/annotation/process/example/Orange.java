package com.my.annotation.process.example;

import com.my.annotaion.process.annotation.Factory;

/**
 * 橘子
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
@Factory(id = "orange", type = Fruit.class)
public class Orange implements Fruit {

    @Override
    public Float getPrice() {
        return 3.5F;
    }
}
