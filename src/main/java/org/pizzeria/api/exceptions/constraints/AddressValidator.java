package org.pizzeria.api.exceptions.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.pizzeria.api.entity.address.Address;
import org.pizzeria.api.exceptions.constraints.annotation.ValidAddress;
import org.pizzeria.api.utils.globals.ValidationResponses;
import org.pizzeria.api.utils.globals.ValidationRules;

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

		if (!address.getStreet().matches(ValidationRules.ADDRESS_STRING_FIELD)) {
			context.buildConstraintViolationWithTemplate(ValidationResponses.ADDRESS_STREET).addPropertyNode("street").addConstraintViolation();
			return false;
		}

		int streetNrDigits = String.valueOf(address.getNumber()).length();
		if (streetNrDigits > 4) {
			context.buildConstraintViolationWithTemplate(ValidationResponses.ADDRESS_STREET_NUMBER).addPropertyNode("number").addConstraintViolation();
			return false;
		}

		if (address.getDetails() != null && !address.getDetails().matches(ValidationRules.ADDRESS_STRING_FIELD)) {
			context.buildConstraintViolationWithTemplate(ValidationResponses.ADDRESS_DETAILS).addPropertyNode("details").addConstraintViolation();
			return false;
		}

		return true;
	}
}