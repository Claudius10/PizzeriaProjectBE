package PizzaApp.api.exceptions.constraints;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.exceptions.constraints.annotation.ValidAddress;
import PizzaApp.api.utils.globals.ValidationResponses;
import PizzaApp.api.utils.globals.ValidationRules;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AddressValidator implements ConstraintValidator<ValidAddress, Address> {

	@Override
	public void initialize(ValidAddress constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Address address, ConstraintValidatorContext context) {
		// When updating user order, only address id is present
		if (address.getId() != null) {
			return true;
		}

		if (!address.getStreet().matches(ValidationRules.ADDRESS_STRING_FIELD)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ValidationResponses.ADDRESS_STREET).addPropertyNode("street").addConstraintViolation();
			return false;
		}

		int streetNrDigits = String.valueOf(address.getStreetNr()).length();
		if (streetNrDigits > 4) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ValidationResponses.ADDRESS_STREET_NUMBER).addPropertyNode("streetNr").addConstraintViolation();
			return false;
		}

		if (address.getGate() != null && !address.getGate().matches(ValidationRules.ADDRESS_STRING_FIELD)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ValidationResponses.ADDRESS_GATE).addPropertyNode("gate").addConstraintViolation();
			return false;
		}

		if (address.getStaircase() != null && !address.getStaircase().matches(ValidationRules.ADDRESS_STRING_FIELD)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ValidationResponses.ADDRESS_STAIRCASE).addPropertyNode("staircase").addConstraintViolation();
			return false;
		}

		if (address.getFloor() != null && !address.getFloor().matches(ValidationRules.ADDRESS_STRING_FIELD)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ValidationResponses.ADDRESS_FLOOR).addPropertyNode("floor").addConstraintViolation();
			return false;
		}

		if (address.getDoor() != null && !address.getDoor().matches(ValidationRules.ADDRESS_STRING_FIELD)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ValidationResponses.ADDRESS_DOOR).addPropertyNode("door").addConstraintViolation();
			return false;
		}

		return true;
	}
}