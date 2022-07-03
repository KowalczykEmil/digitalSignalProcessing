package model.operation.twoSignal;

import addons.OperationException;
import model.signal.Signal;

public interface SignalOperation {

	public Signal execute(Signal first, Signal second) throws OperationException;

	public String getName();

}
