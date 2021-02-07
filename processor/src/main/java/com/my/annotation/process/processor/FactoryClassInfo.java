package com.my.annotation.process.processor;

import com.my.annotaion.process.annotation.Factory;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * 工厂类信息
 *
 * @author Zijian Liao
 * @since 1.0.0
 */
public class FactoryClassInfo {
    /**
     * 子类的类元信息
     */
    private final TypeElement typeElement;
    /**
     * 组件的id, 用于确认生成哪个组件
     */
    private final String id;
    /**
     * 注解上的值的类名信息
     */
    private String simpleName;
    /**
     * 注解上的值的全路径限定类名信息
     */
    private String qualifiedName;

    public FactoryClassInfo(Element element){
        this.typeElement = (TypeElement) element;
        // 获取注解信息
        Factory annotation = typeElement.getAnnotation(Factory.class);
        // apple
        this.id = annotation.id();
        try{
            // 获取注解的值 若该类未被编译，此处将抛出异常
            Class<?> type = annotation.type();
            // 获取注解上的全路径限定名信息
            this.qualifiedName = type.getCanonicalName();
            // 获取注解上的类名信息
            this.simpleName = type.getSimpleName();
        }catch (MirroredTypeException e){
            // 所幸的是，该异常里也具备了我们需要的信息
            DeclaredType classTypeMirror = (DeclaredType) e.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            // com.my.annotation.process.example.Fruit
            this.qualifiedName = classTypeElement.getQualifiedName().toString();
            // Fruit
            this.simpleName = classTypeElement.getSimpleName().toString();
        }
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public String getId() {
        return id;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

}
