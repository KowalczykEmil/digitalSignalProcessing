package addons;

import javafx.scene.control.TextFormatter;

import java.util.regex.Pattern;

public class NumberFormatter {

	public static TextFormatter<Double> getIntegerFormatter() {
		Pattern decimalPattern = Pattern.compile("[0-9]{0,2}");
		return new TextFormatter<>(c -> (decimalPattern.matcher(c.getControlNewText()).matches()) ? c : null);
	}

	public static TextFormatter<Double> getDecimalFormatter() {
		Pattern decimalPattern = Pattern.compile("-?\\d*(\\.\\d{0,4})?");
		return new TextFormatter<>(c -> (decimalPattern.matcher(c.getControlNewText()).matches()) ? c : null);
	}

	public static boolean validInteger(String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		if (!value.matches("\\d{1,}")) {
			return false;
		}
		return true;
	}

	public static boolean validDouble(String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		if (!value.matches("-?\\d*(\\.\\d{0,10})?")) {
			return false;
		}
		return true;
	}

}
