package PizzaApp.api.exceptions.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Null values are not considered valid.
 *
 * @author Claudiu Catalin
 */

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LongLengthValidator.class)

public @interface LongLength {
	String message() default "Valor númerico no acceptado";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	long min();

	long max();
}