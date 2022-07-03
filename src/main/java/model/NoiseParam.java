package model;

import lombok.Data;

import static model.noise.AbstractNoise.SAMPLE_DIST;

@Data
public class NoiseParam {

	private Double amplitude;
	private Double initialTime;
	private Double duration;
	private Double basePeriod;
	private Double fillFactor;
	private Double sampling = SAMPLE_DIST;

}
