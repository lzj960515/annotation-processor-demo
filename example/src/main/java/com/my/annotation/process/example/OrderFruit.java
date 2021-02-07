package com.my.annotation.process.example;

/**
 * 订水果
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
public class OrderFruit {

    /**
     * 获取水果价格
     *
     * @param fruitName 水果名称
     * @return 水果价格
     */
    public Float order(String fruitName){
        return FruitFactory.create(fruitName).getPrice();
    }
}
