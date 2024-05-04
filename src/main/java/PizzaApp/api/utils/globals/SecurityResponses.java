package PizzaApp.api.utils.globals;

public final class SecurityResponses {

	public final static String USER_ID_MISSING = "Acceso denegado: no se puede verificar la identidad del usuario.";

	public final static String MISSING_TOKEN = "Acceso denegado: falta el token.";

	public final static String FRAUDULENT_TOKEN = "Acceso denegado: solicitud fraudulenta.";

	public final static String BAD_CREDENTIALS = "El correo eléctronico o la contraseña introducida es incorrecta.";

	private SecurityResponses() {
	}
}