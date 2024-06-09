package PizzaApp.api.exceptions.constraints.annotation;

import PizzaApp.api.exceptions.constraints.IntegerLengthValidator;
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
@Constraint(validatedBy = IntegerLengthValidator.class)
public @interface IntegerLength {
	String message() default "Valor numérico no aceptado";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int min();

	int max();
}
