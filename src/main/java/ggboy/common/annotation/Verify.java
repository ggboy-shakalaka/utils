package ggboy.common.annotation;

import ggboy.common.enums.ParameterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Verify {
	ParameterType Type() default ParameterType.STRING;
	boolean NotNull() default false;
	int maxLength() default -1;
	int minLength() default -1;
}
