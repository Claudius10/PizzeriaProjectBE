package PizzaApp.api.exceptions.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DoubleLengthValidator implements ConstraintValidator<DoubleLengthNullable, Double> {

	private double min, max;

	@Override
	public void initialize(DoubleLengthNullable doubleLength) {
		this.min = doubleLength.min();
		this.max = doubleLength.max();
	}

	@Override
	public boolean isValid(Double value, ConstraintValidatorContext context) {
		// validate whatever the int field value is inbetween min and max
		if (value != null) {
			return (String.valueOf(value.doubleValue()).length() >= min
					&& String.valueOf(value.doubleValue()).length() <= max);
		} else {
			// return true if value is null
			return true;
		}
	}

}
