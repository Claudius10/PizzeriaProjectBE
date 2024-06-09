package PizzaApp.api.exceptions.constraints.annotation;

import PizzaApp.api.exceptions.constraints.AddressValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {AddressValidator.class})
@Documented
public @interface ValidAddress {

	String message() default "El formato del domicilio no es válido";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
