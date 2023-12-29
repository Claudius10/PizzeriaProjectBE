package PizzaApp.api.utility.string;

public final class StringUtils {

	private StringUtils() {
	}

	public static boolean isNumber(String string) {
		if (string == null) {
			return false;
		}

		try {
			long l = Long.parseLong(string);
		} catch (NumberFormatException nfe) {
			return false;
		}

		return true;
	}
}
