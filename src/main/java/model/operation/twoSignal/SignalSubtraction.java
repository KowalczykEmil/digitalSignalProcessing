package model.operation.twoSignal;

public class SignalSubtraction extends AbstractSignalOperation {

	@Override
	public String getName() {
		return "odejmij";
	}

	@Override
	double calculate(Double first, Double second) {
		return first - second;
	}
}
