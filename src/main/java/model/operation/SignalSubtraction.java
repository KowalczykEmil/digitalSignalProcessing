package model.operation;

public class SignalSubtraction extends AbstractSignalOperation {

	@Override
	public String getName() {
		return "odejmin";
	}

	@Override
	double calculate(Double first, Double second) {
		return first - second;
	}
}
