package org.pizzeria.api.utils.globals;

public final class ValidationResponses {

	public static final String NAME_INVALID = "El nombre y apellido(s) debe tener una longitud entre 2-50 letras." +
			" Incluye tildes. No incluye símbolos especiales.";

	public static final String EMAIL_INVALID = "Formato inválido de correo electrónico. Ejemplo válido: marcopolo22@gmail.com";

	public static final String EMAIL_MISSING = "El correo electrónico no puede faltar.";

	public static final String EMAIL_NO_MATCH = "El correo electrónico debe coincidir.";

	public static final String PASSWORD_INVALID = "La contraseña debe tener una longitud mínima de 8 valores " +
			"alfanuméricos, incluir una letra mayúscula y un número.";

	public static final String PASSWORD_MISSING = "La contraseña no puede faltar.";

	public static final String PASSWORD_NO_MATCH = "La contraseña debe coincidir.";

	public static final String NUMBER_INVALID = "El teléfono de contacto debe tener una longitud fija de 9 " +
			"dígitos.";

	public static final String ADDRESS_STREET = "La dirección no debe incluir símbolos especiales y su longitud debe " +
			"tener entre 2-25 letras.";

	public static final String ADDRESS_STREET_NUMBER = "El número debe tener una longitud máxima de 4 dígitos.";

	public static final String ADDRESS_GATE = "El portal no debe incluir símbolos especiales y su longitud debe tener " +
			"un máximo de 25 letras.";

	public static final String ADDRESS_STAIRCASE = "La escalera no debe incluir símbolos especiales y su longitud debe " +
			"tener un máximo de 25 letras.";

	public static final String ADDRESS_FLOOR = "El piso no debe incluir símbolos especiales y su longitud debe tener un" +
			" máximo de 25 letras.";

	public static final String ADDRESS_DOOR = "La puerta no debe incluir símbolos especiales y su longitud debe tener " +
			"un máximo de 25 letras.";

	public static final String ORDER_DETAILS_DELIVERY_HOUR = "La hora de entrega no puede faltar.";

	public static final String ORDER_DETAILS_PAYMENT = "La forma de pago no puede faltar.";

	public static final String ORDER_DETAILS_CHANGE_REQUESTED_LENGTH = "El cambio solicitado es inválido. Ejemplo válido: 25.55";

	public static final String ORDER_DETAILS_CHANGE_REQUESTED = "El valor del cambio de efectivo solicitado no puede " +
			"ser menor o igual que el total o total con ofertas.";

	public static final String ORDER_DETAILS_COMMENT = "El campo 'Observaciones al repartidor' debe tener una longitud máxima " +
			"de 150 valores alfanuméricos. Incluye tildes e y los símbolos !¡ ?¿ . , : ;";

	public static final String CART_IS_EMPTY = "La cesta no debe estar vacía.";

	public static final String CART_COST_INVALID = "El formato del coste de la cesta es inválido. Ejemplo válido: 255.55";

	public static final String CART_ITEM_TYPE_MISSING = "El tipo del artículo no puede faltar.";

	public static final String CART_ITEM_NAME_MISSING = "El nombre del artículo no puede faltar.";

	public static final String CART_ITEM_FORMAT_MISSING = "El formato del artículo no puede faltar.";

	public static final String CART_ITEM_MAX_PRICE = "Formato del precio válido: 12.55€, 10, 11.5.";

	public static final String ORDER_UPDATE_TIME_ERROR = "El tiempo límite para actualizar el pedido (15 minutos) ha finalizado.";

	public static final String ORDER_DELETE_TIME_ERROR = "El tiempo límite para anular el pedido (20 minutos) ha finalizado.";

	public static final String CART_MAX_PRODUCTS_QUANTITY_ERROR = "Se ha superado el límite de artículos por pedido (20). " +
			"Contacte con " +
			"nosotros si desea realizar el pedido.";

	public static final String CART_ITEM_MAX_QUANTITY_ERROR = "Se ha superado el límite de unidades por artículo (20). " +
			"Contacte con nosotros si desea realizar el pedido.";

	private ValidationResponses() {
	}
}