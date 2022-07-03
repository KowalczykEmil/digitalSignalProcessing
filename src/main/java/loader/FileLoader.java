package loader;

import javafx.scene.control.*;

public class FileLoader {
	public ComboBox signalTypeSelection;
	public Button generateButton;

	public Label labelAmplitude;
	public Label labelInitialTime;
	public Label labelDuration;
	public Label labelBasePeriod;
	public Label labelFillFactor;
	public Label labelHistogramIntervals;

	public TextField textFieldAmplitude;					  // Amplituda
	public TextField textFieldBasePeriod;					  // Okres podstawowy
	public TextField textFieldInitialTime;					  // Czas początkowy
	public TextField textFieldDuration;						  // Czas trwania
	public TextField textFieldFillFactor;                     // Współczynnik wypelnienia -> Trojkatny / Prostokątny
	public TextField textFieldHistogramIntervals;			  // Przedział histogramu

}
