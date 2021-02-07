package com.my.annotation.process.processor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理器，用于生成Java File
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
public class FactoryHandler {

    /**
     * 创建出的类名后缀
     */
    private static final String SUFFIX = "Factory";
    /**
     * 存放类信息
     */
    private static final Map<String, List<FactoryClassInfo>> FACTORY_CLASS_INFO_MAP = new ConcurrentHashMap<>(32);

    /**
     * 存放类信息
     * @param factoryClassInfo 类信息
     */
    public static void putIfAbsent(FactoryClassInfo factoryClassInfo){
        String qualifiedName = factoryClassInfo.getQualifiedName();
        if(!FACTORY_CLASS_INFO_MAP.containsKey(qualifiedName)){
            FACTORY_CLASS_INFO_MAP.put(qualifiedName, new ArrayList<>(4));
        }
        List<FactoryClassInfo> factoryClassInfos = FACTORY_CLASS_INFO_MAP.get(qualifiedName);
        factoryClassInfos.add(factoryClassInfo);
    }

    /**
     * 生成Java文件
     * @param elementUtils 元素工具
     * @param filer 文件工具
     * @throws IOException 异常
     */
    public static void generateJavaFile(Elements elementUtils, Filer filer) throws IOException {
        if(FACTORY_CLASS_INFO_MAP.isEmpty()){
            return;
        }
        for (Map.Entry<String, List<FactoryClassInfo>> entry : FACTORY_CLASS_INFO_MAP.entrySet()) {
            String qualifiedName = entry.getKey();
            List<FactoryClassInfo> factoryClassInfos = entry.getValue();
            // 1.得到接口（抽象类）的类名信息
            TypeElement superClassElement = elementUtils.getTypeElement(qualifiedName);
            // 2.创建出工厂方法
            MethodSpec.Builder method = MethodSpec.methodBuilder("create") // 方法名
                    .addParameter(String.class, "id") //方法参数 （类型｜名称）
                    .addModifiers(Modifier.PUBLIC) // 修饰符
                    .addModifiers(Modifier.STATIC)
                    .returns(TypeName.get(superClassElement.asType())); // 返回类型
            // 3.遍历子类创建出代码信息
            factoryClassInfos.forEach(factoryClassInfo -> {
                // $S 表示String类型占位符
                // $L 表示名称占位符 给啥就是啥
                method.beginControlFlow("if($S.equals(id))", factoryClassInfo.getId()) // 开启一个控制语句
                        .addStatement("return new $L()", factoryClassInfo.getTypeElement().getQualifiedName().toString()) // 添加一条语句
                        .endControlFlow(); // 结束控制语句
            });
            // 由于工厂方法需要返回值，若最后都不匹配则抛出异常
            // 添加一条抛异常语句
            method.addStatement("throw new IllegalArgumentException($S + id)", "Unknown id = ");
            // 4. 创建工厂类
            // 接口（抽象类）的类名
            String superSimpleName = superClassElement.getSimpleName().toString();
            // 类名则为 superSimpleName + Factory
            String className = superSimpleName + SUFFIX;
            TypeSpec typeSpec = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(method.build())
                    .build();
            // 5.写入java文件
            // 获取到包名信息
            PackageElement packageElement = elementUtils.getPackageOf(superClassElement);
            String packageName = packageElement.getQualifiedName().toString();
            JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
        }
    }

    public static void clear(){
        FACTORY_CLASS_INFO_MAP.clear();
    }
}
