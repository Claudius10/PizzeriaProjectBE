package PizzaApp.api.entity.order.dto;

import PizzaApp.api.entity.address.Address;
import PizzaApp.api.entity.order.Cart;
import PizzaApp.api.entity.order.OrderDetails;
import PizzaApp.api.exceptions.constraints.IntegerLength;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record NewAnonOrderDTO(

		@Pattern(regexp = "^[a-zA-Z\sÁÉÍÓÚáéíóúÑñ]{2,50}$",
				message = "Compruebe que el nombre y apellido(s) esté compuesto solo por un mínimo de 2 y un máximo de 50 letras")
		String anonCustomerName,

		@IntegerLength(min = 9, max = 9, message = "Compruebe que el número de teléfono tenga 9 dígitos")
		Integer anonCustomerContactNumber,

		@Email(message = "Compruebe el email introducido. Ejemplo: jonas15@gmail.com")
		String anonCustomerEmail,

		@Valid
		Address address,

		@Valid
		OrderDetails orderDetails,

		Cart cart) {
}
