package PizzaApp.api.exceptions.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldMatch {
	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String first();

	String second();

	@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		FieldMatch[] value();
	}
}