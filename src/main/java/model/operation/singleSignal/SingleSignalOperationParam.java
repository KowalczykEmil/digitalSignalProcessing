package model.operation.singleSignal;

import lombok.Data;

@Data
public class SingleSignalOperationParam {
	private Double samplingFrequency;
	private Double quantizationStep;
	private Integer neighbourSamples;

}
