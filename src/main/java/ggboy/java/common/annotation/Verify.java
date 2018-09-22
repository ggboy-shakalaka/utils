package ggboy.java.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ggboy.java.common.enums.ParameterType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Verify {
	ParameterType Type() default ParameterType.STRING;
	boolean NotNull() default false;
	int maxLength() default -1;
	int minLength() default -1;
}
