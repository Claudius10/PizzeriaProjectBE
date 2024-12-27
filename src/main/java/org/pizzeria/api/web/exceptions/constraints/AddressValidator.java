package org.pizzeria.api.web.exceptions.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.web.exceptions.constraints.annotation.ValidAddress;
import org.pizzeria.api.web.constants.ValidationResponses;
import org.pizzeria.api.web.constants.ValidationRules;

public class AddressValidator implements ConstraintValidator<ValidAddress, Address> {

	@Override
	public void initialize(ValidAddress constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Address address, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();

		// When updating user order, only address id is present
		if (address.getId() != null) {
			return true;
		}

		if (!address.getStreet().matches(ValidationRules.SIMPLE_LETTERS_ONLY_MAX_25_INSENSITIVE_REQUIERED)) {
			context.buildConstraintViolationWithTemplate(ValidationResponses.ADDRESS_STREET).addPropertyNode("street").addConstraintViolation();
			return false;
		}

		int streetNrDigits = String.valueOf(address.getNumber()).length();
		if (streetNrDigits > 4) {
			context.buildConstraintViolationWithTemplate(ValidationResponses.ADDRESS_STREET_NUMBER).addPropertyNode("number").addConstraintViolation();
			return false;
		}

		if (address.getDetails() != null && !address.getDetails().matches(ValidationRules.COMPLEX_LETTERS_NUMBERS_MAX_150_OPTIONAL)) {
			context.buildConstraintViolationWithTemplate(ValidationResponses.ADDRESS_DETAILS).addPropertyNode("details").addConstraintViolation();
			return false;
		}

		return true;
	}
}