package model.operation;

public class SignalAddition extends AbstractSignalOperation {

	@Override
	public String getName() {
		return "dodaj";
	}

	@Override
	double calculate(Double first, Double second) {
		return first + second;
	}
}
