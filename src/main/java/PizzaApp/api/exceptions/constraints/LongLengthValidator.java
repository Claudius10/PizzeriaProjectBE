package PizzaApp.api.exceptions.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LongLengthValidator implements ConstraintValidator<LongLength, Long> {

	private long min, max;

	@Override
	public void initialize(LongLength integerLength) {
		this.min = integerLength.min();
		this.max = integerLength.max();
	}

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		// validate whatever value is null
		// and
		// validate whatever the long field value is inbetween min and max
		return (value != null && (String.valueOf(value.longValue()).length() >= min
				&& String.valueOf(value.longValue()).length() <= max));
	}
}
