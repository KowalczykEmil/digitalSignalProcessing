package model.operation.window;

import addons.OperationException;
import model.operation.filter.Filter;
import model.signal.Signal;

public interface Window {
	public Signal execute(Filter filter, int rzadFiltru, double czestotliwoscOdciecia, double czestotliwoscProbkowania) throws OperationException;

	String getName();
}
