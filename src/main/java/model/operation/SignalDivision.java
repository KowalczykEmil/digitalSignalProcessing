package model.operation;

public class SignalDivision extends AbstractSignalOperation {

	@Override
	public String getName() {
		return "podziel";
	}

	@Override
	double calculate(Double first, Double second) {
		if (!second.equals(0d)) {
			return first / second;
		} else {
			return 0;
		}
	}
}
