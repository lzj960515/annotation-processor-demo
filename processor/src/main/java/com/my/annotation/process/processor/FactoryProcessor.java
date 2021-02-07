package com.my.annotation.process.processor;


import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.my.annotaion.process.annotation.Factory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

/**
 * 工厂类生成器
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
@AutoService(Processor.class)
public class FactoryProcessor extends AbstractProcessor {
    /**
     * 文件写入器，用于生成class文件
     */
    private Filer filer;
    /**
     * 消息发送器，用于打印消息
     */
    private Messager messager;
    /**
     * 元素操作工具
     */
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
        this.elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(Factory.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 获取到标记了@Factory的类
        try{
            Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(Factory.class);
            for (Element element : elementsAnnotatedWith) {
                FactoryHandler.putIfAbsent(new FactoryClassInfo(element));
            }
            FactoryHandler.generateJavaFile(elementUtils, filer );
        }catch (IOException e){
            error(null, e.getMessage());
        }finally {
            // 重要：最后需要把存储的信息清除
            // 原因：注解处理按一系列回合进行的，当第一个回合生成了新的文件，那么processor便会使用这些的文件进行第二个回合
            // 如果我们不清除这些第一个回合存储的信息，那么在第二个回合将会被重复使用！这将引起文件重复创建的错误！
            FactoryHandler.clear();
        }
        return true;
    }

    /**
     * 打印错误信息
     *
     * @param e 用作位置提示的元素
     * @param message 错误信息
     */
    private void error(Element e, String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, e);
    }
}
