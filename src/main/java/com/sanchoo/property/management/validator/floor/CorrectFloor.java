package com.sanchoo.property.management.validator.floor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectFloorValidator.class)
@Documented
public @interface CorrectFloor {
	String message() default "*Номер этажа не может быть меньше этажности!";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
