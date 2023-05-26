package PizzaApp.api.validation.constraints;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Null values are not considered valid.
 * @author Claudiu Catalin
 */

@Documented
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IntegerLengthValidator.class)

public @interface IntegerLength {
	String message() default "Valor númerico no acceptado";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
	int min();
	
	int max();
}
