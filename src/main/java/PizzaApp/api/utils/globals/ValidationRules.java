package PizzaApp.api.utils.globals;

public final class ValidationRules {

	public static final String ADDRESS_STRING_FIELD = "^[a-zA-Z\sÁÉÍÓÚáéíóúÑñ-]{2,25}$";

	public static final String USER_NAME = "^[a-zA-Z\sÁÉÍÓÚáéíóúÑñ]{2,50}$";

	public static final String ORDER_DETAILS_COMMENT = "^[a-zA-Z0-9ÁÉÍÓÚáéíóúÑñ!¡¿?.,\s]{0,150}$";

	public static final String USER_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
}