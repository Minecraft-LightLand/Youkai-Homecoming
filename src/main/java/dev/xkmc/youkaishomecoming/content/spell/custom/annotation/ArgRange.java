package dev.xkmc.youkaishomecoming.content.spell.custom.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ArgRange {

	int low() default 0;

	int high() default 0;

	int base() default 0;

	int factor() default 0;

	int decimal() default 0;

}
