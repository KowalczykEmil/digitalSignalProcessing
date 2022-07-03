package model.operation.filter;

import java.util.List;

public interface Filter {
	public List<Double> execute(int rzadFiltru, double czestotliwoscOdciecia, double czestotliwoscProbkowania);

	String getName();
}
