package com.my.annotaion.process.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记为一个工厂组件
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Factory {

    /**
     * 组件的id, 用于确认生成哪个组件
     */
    String id();

    /**
     * 工厂的类型
     */
    Class<?> type();
}
