package org.pizzeria.api.exceptions.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.pizzeria.api.exceptions.constraints.annotation.DoubleLength;

public class DoubleLengthValidator implements ConstraintValidator<DoubleLength, Double> {

	private double min, max;

	@Override
	public void initialize(DoubleLength doubleLength) {
		this.min = doubleLength.min();
		this.max = doubleLength.max();
	}

	@Override
	public boolean isValid(Double value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}

		int length = String.valueOf(value.doubleValue()).length();
		return (length >= min && length <= max);
	}
}