package PizzaApp.api.exceptions.constraints;

import PizzaApp.api.entity.user.Address;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidAddressValidator implements ConstraintValidator<ValidAddress, Address> {

	@Override
	public void initialize(ValidAddress constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Address address, ConstraintValidatorContext context) {
		if (address.getId() != null) {
			return true;
		}

		if (!address.getStreet().matches("^[a-zA-Z.,:;()\sÁÉÍÓÚáéíóúÑñ-]{2,25}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("La calle solo puede contener entre 2 y 25 letras y los símbolos . , : ; ( ) -")
					.addPropertyNode("street").addConstraintViolation();
			return false;
		}

		if (String.valueOf(address.getStreetNr()).length() < 1 && String.valueOf(address.getStreetNr()).length() > 4) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("El número de la calle solo puede contener un máximo de 4 digitos")
					.addPropertyNode("streetNr").addConstraintViolation();
			return false;
		}

		if (address.getGate() != null && !address.getGate().matches("^[a-zA-Z0-9.,:;()\sÁÉÍÓÚáéíóúÑñ-]{0,25}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("La puerta solo puede contener un máximo de 25 letras y/o digitos, y " +
							"los " +
							"símbolos " +
							". " +
							", : ; ( ) -")
					.addPropertyNode("gate").addConstraintViolation();
			return false;
		}

		if (address.getStaircase() != null && !address.getStaircase().matches("^[a-zA-Z0-9.,:;()\sÁÉÍÓÚáéíóúÑñ-]{0,25}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("La escalera solo puede contener un máximo de 25 letras y/o digitos, y los " +
							"símbolos ." +
							" " +
							", : ; ( ) -")
					.addPropertyNode("staircase").addConstraintViolation();
			return false;
		}

		if (address.getFloor() != null && !address.getFloor().matches("^[a-zA-Z0-9.,:;()\sÁÉÍÓÚáéíóúÑñ-]{0,25}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("El piso solo puede contener un máximo de 25 letras y/o digitos, y los " +
							"símbolos ." +
							" " +
							", : ; ( ) -")
					.addPropertyNode("floor").addConstraintViolation();
			return false;
		}

		if (address.getDoor() != null && !address.getDoor().matches("^[a-zA-Z0-9.,:;()\sÁÉÍÓÚáéíóúÑñ-]{0,25}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("La puerta solo puede contener un máximo de 25 letras y/o digitos, y " +
							"los " +
							"símbolos ." +
							" " +
							", : ; ( ) -")
					.addPropertyNode("door").addConstraintViolation();
			return false;
		}

		return true;
	}
}
