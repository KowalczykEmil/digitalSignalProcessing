package model.operation;

public class SignalMultiplication extends AbstractSignalOperation {

	@Override
	public String getName() {
		return "pomnóż";
	}

	@Override
	double calculate(Double first, Double second) {
		return first * second;
	}
}
