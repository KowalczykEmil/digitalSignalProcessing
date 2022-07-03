package model.noise;

import loader.FIleLoader;
import model.NoiseParam;
import model.signal.Signal;

public interface Noise {

	public String getName();

	public Signal generate();

	public void setParams(NoiseParam params);

	public void setInputVisibility(FIleLoader editor);
}
