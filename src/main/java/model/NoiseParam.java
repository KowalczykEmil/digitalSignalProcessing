package model;

import lombok.Data;

@Data
public class NoiseParam {

	private Double amplitude;
	private Double initialTime;
	private Double duration;
	private Double basePeriod;
	private Double fillFactor;
	private Double samplingPeriod;


	public NoiseParam copyOf() {
		NoiseParam outputNoiseParam = new NoiseParam();
		outputNoiseParam.setSamplingPeriod(samplingPeriod);
		outputNoiseParam.setAmplitude(amplitude);
		outputNoiseParam.setInitialTime(initialTime);
		outputNoiseParam.setDuration(duration);
		outputNoiseParam.setBasePeriod(duration);
		outputNoiseParam.setFillFactor(fillFactor);
		return outputNoiseParam;
	}

}
