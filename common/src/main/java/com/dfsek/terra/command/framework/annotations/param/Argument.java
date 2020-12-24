package com.dfsek.terra.command.framework.annotations.param;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {

    String name() default "";

    String description();

    String[] suggestions() default {};

    String defaultValue() default "";

    String suggestionSupplierMethod() default "";

//    boolean optional() default false;
}
