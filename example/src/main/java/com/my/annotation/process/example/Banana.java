package com.my.annotation.process.example;

import com.my.annotaion.process.annotation.Factory;

/**
 * 香蕉
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
@Factory(id = "banana", type = Fruit.class)
public class Banana implements Fruit{

    @Override
    public Float getPrice() {
        return 8F;
    }
}
