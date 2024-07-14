package org.pizzeria.api.utils.globals;

public final class SecurityResponses {

	public static final String USER_ID_MISSING = "Acceso denegado: no se puede verificar la identidad del usuario.";

	public static final String MISSING_TOKEN = "Acceso denegado: falta el token.";

	public static final String FRAUDULENT_TOKEN = "Acceso denegado: solicitud fraudulenta.";

	public static final String BAD_CREDENTIALS = "El correo eléctronico o la contraseña introducida es incorrecta.";

	private SecurityResponses() {
	}
}