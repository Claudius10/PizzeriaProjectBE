package org.pizzeria.api.web.globals;

public final class ApiResponses {

	public static final String USER_NOT_FOUND = "UsernameNotFoundException";

	public static final String USER_EMAIL_ALREADY_EXISTS = "EmailAlreadyExists";

	public static final String USER_NUMBER_ALREADY_EXISTS = "UserNumberAlreadyExists";

	public static final String ADDRESS_NOT_FOUND = "UserAddressNotFound";

	public static final String ADDRESS_LIST_EMPTY = "UserAddressListEmpty";

	public static final String ADDRESS_MAX_SIZE = "UserAddressListFull";

	public static final String USER_ORDER_UPDATE_ERROR = "UserOrderUpdateError";

	public static final String ORDER_NOT_FOUND = "UserOrderNotFound";

	public static final String ORDER_LIST_EMPTY = "UserOrderListEmpty";

	private ApiResponses() {
	}
}