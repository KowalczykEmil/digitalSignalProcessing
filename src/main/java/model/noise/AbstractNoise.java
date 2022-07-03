package model.noise;

import loader.FIleLoader;
import javafx.scene.chart.XYChart;
import model.NoiseParam;
import model.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNoise implements Noise {
	public final static double SAMPLE_DIST = 0.001; //[s]
	List<XYChart.Data<Double, Double>> dataset;
	Signal signal;
	NoiseParam params;

	@Override
	public Signal generate() {
		this.signal = getSignalType();
		dataset = new ArrayList<>();
		generateDataSeries();
		correctParams();
		signal.generateSignal(params, dataset);
		return signal;
	}

	protected void correctParams(){
	}

	@Override
	public void setInputVisibility(FIleLoader editor) {
		editor.textFieldAmplitude.setVisible(isAmplitudeEditorVisible());
		editor.textFieldInitialTime.setVisible(isInitialTimeEditorVisible());
		editor.textFieldDuration.setVisible(isDurationEditorVisible());
		editor.textFieldBasePeriod.setVisible(isBasePeriodEditorVisible());
		editor.textFieldFillFactor.setVisible(isFillFactorEditorVisible());

		editor.labelAmplitude.setVisible(isAmplitudeEditorVisible());
		editor.labelInitialTime.setVisible(isInitialTimeEditorVisible());
		editor.labelDuration.setVisible(isDurationEditorVisible());
		editor.labelBasePeriod.setVisible(isBasePeriodEditorVisible());
		editor.labelFillFactor.setVisible(isFillFactorEditorVisible());
	}

	protected boolean isAmplitudeEditorVisible() {
		return true;
	}

	protected boolean isInitialTimeEditorVisible() {
		return true;
	}

	protected boolean isDurationEditorVisible() {
		return true;
	}

	protected boolean isBasePeriodEditorVisible() {
		return true;
	}

	protected boolean isFillFactorEditorVisible() {
		return true;
	}

	@Override
	public void setParams(NoiseParam params) {
		if (!isAmplitudeEditorVisible()) {
			params.setAmplitude(null);
		}
		if (!isInitialTimeEditorVisible()) {
			params.setInitialTime(null);
		}
		if (!isDurationEditorVisible()) {
			params.setDuration(null);
		}
		if (!isBasePeriodEditorVisible()) {
			params.setBasePeriod(null);
		}
		if (!isFillFactorEditorVisible()) {
			params.setFillFactor(null);
		}
		this.params = params;
	}

	protected abstract List<XYChart.Data<Double, Double>> generateDataSeries();

	protected abstract Signal getSignalType();
}
