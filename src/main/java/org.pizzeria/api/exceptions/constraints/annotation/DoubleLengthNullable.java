package org.pizzeria.api.exceptions.constraints.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.pizzeria.api.exceptions.constraints.DoubleLengthNullableValidator;

import java.lang.annotation.*;

/**
 * Null values are considered valid. The decimal separating dot (.) counts as 1
 * to the length.
 *
 * @author Claudiu Catalin
 */

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DoubleLengthNullableValidator.class)
@Documented
public @interface DoubleLengthNullable {
	String message() default "Valor numérico no aceptado";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	double min();

	double max();
}