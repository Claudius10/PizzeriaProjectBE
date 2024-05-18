package PizzaApp.api.utils.globals;

public final class ValidationResponses {

	public static final String NAME_INVALID = "El nombre y apellido(s) debe tener una longitud entre 2-50 letras." +
			" Incluye tildes. No incluye símbolos especiales.";

	public static final String EMAIL_INVALID = "Formato inválido de correo electrónico. Ejemplo válido: marcopolo22@gmail.com";

	public static final String EMAIL_MISSING = "El correo electrónico no puede faltar.";

	public static final String EMAIL_NO_MATCH = "El correo electrónico debe coincidir.";

	public static final String EMAIL_ALREADY_EXISTS = "Ya existe una cuenta con el correo electrónico introducido. Si no " +
			"recuerda la contraseña, contacte con nosotros.";

	public static final String PASSWORD_INVALID = "La contraseña debe tener una longitud mínima de 8 valores " +
			"alfanuméricos, incluir una letra mayúscula y un número.";

	public static final String PASSWORD_MISSING = "La contraseña no puede faltar.";

	public static final String PASSWORD_NO_MATCH = "La contraseña debe coincidir.";

	public static final String NUMBER_INVALID = "El teléfono de contacto debe tener una longitud fija de 9 " +
			"dígitos.";

	public static final String ADDRESS_MAX_SIZE = "La cantidad máxima de domicilios por usuario no debe exceder de tres " +
			"domicilios.";

	public static final String ADDRESS_NOT_FOUND = "El domicilio a eliminar no existe en la base de datos.";

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

	public static final String ORDER_DETAILS_CHANGE_REQUESTED_LENGTH = "El cambio solicitado es inválido. Ejemplo " +
			"válido: 25.55.";
	public static final String ORDER_DETAILS_CHANGE_REQUESTED = "El valor del cambio de efectivo solicitado no puede " +
			"ser menor o igual que el total o total con ofertas.";

	public static final String ORDER_DETAILS_COMMENT = "El campo 'Observaciones al repartidor' debe tener una longitud máxima " +
			"de 150 valores alfanuméricos. Incluye tildes e y los símbolos !¡ ?¿ . , : ;";

	public static final String CART_IS_EMPTY = "La cesta no debe estar vacía.";

	private ValidationResponses() {
	}
}