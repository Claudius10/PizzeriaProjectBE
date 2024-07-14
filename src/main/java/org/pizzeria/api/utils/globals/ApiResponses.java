package org.pizzeria.api.utils.globals;

public final class ApiResponses {

	public static final String ADDRESS_NOT_FOUND = "El domicilio a eliminar no existe en la base de datos.";

	public static final String ADDRESS_LIST_EMPTY = "La lista de domicilios está vacía.";

	public static final String EMAIL_ALREADY_EXISTS = "Email asociado a una cuenta existente. Por favor, utilice esa cuenta o " +
			"contacte con nosotros.";

	public static final String NUMBER_ALREADY_EXISTS = "Número de contacto asociado a una cuenta existente. Por favor, utilice " +
			"esa cuenta o contacte con nosotros.";

	public static final String UPDATE_USER_ORDER_ERROR = "El pedido con id %s o la dirección con id %s no se puede encontrar.";

	public static final String ORDER_NOT_FOUND = "El pedido con id %s no se puede encontrar.";

	public static final String ORDER_LIST_EMPTY = "La lista de pedidos está vacía.";

	public static final String USER_NOT_FOUND = "El usuario con id %s no se puede encontrar.";

	public static final String USER_EMAIL_NOT_FOUND = "El usuario con correo electrónico %s no existe.";

	public static final String ADDRESS_MAX_SIZE = "La cantidad máxima de domicilios por usuario no debe exceder de tres " +
			"domicilios.";

	private ApiResponses() {
	}
}