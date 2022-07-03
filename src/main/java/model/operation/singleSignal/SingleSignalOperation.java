package model.operation.singleSignal;

import addons.OperationException;
import model.signal.Signal;

public interface SingleSignalOperation {

	public Signal execute(Signal signal, SingleSignalOperationParam param) throws OperationException;

	public String getName();

}
