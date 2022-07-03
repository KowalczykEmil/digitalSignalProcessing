package gui.distance;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.signal.ContinuousSignal;
import model.signal.DiscreteSignal;
import model.signal.Signal;
import gui.graph.ChartPane;

import java.util.ArrayList;
import java.util.List;

import static addons.NumberFormatter.getDecimalFormatter;

public class ParamPane implements EventHandler {
	VBox vPane;

	Button generate;
	TextField liczbaPomiarowTextField;
	TextField predkoscRzeczywistaTextField;
	TextField predkoscAbstrakcyjnaTextField;
	TextField okresSygnaluTextField;
	TextField liczbaPodstawowychSygnalowTextField;
	TextField czestotliwoscProbkowaniaTextField;
	TextField dlugoscBuforowTextField;
	TextField okresRaportowaniaTextField;

	ListView<Double> emittedDistanceList;
	ListView<Double> returningDistanceList;

	ChartPane emittedPane;
	ChartPane returningPane;
	ChartPane correlationPane;

	public static final ObservableList originalDistance =
			FXCollections.observableArrayList();
	public static final ObservableList returnDistance =
			FXCollections.observableArrayList();

	private Signal signal;

	public ParamPane() {
		this.vPane = new VBox();
		vPane.setPadding(new Insets(10, 10, 10, 10));
		buildView();
	}


	private void buildView() {

		emittedPane = new ChartPane(  new ScatterChart<Number, Number>(new NumberAxis(), new NumberAxis()));
		returningPane = new ChartPane(  new ScatterChart<Number, Number>(new NumberAxis(), new NumberAxis()));
		correlationPane = new ChartPane(  new ScatterChart<Number, Number>(new NumberAxis(), new NumberAxis()));

		Label liczbaPomiarowLabel = new Label("Liczba pomiarow:");
		Label predkoscRzeczywistaLabel = new Label("Prędkośc rzeczywista:");
		Label predkoscAbstrakcyjnaLabel = new Label("Prędkość w ośrodku:");
		Label okresSygnaluLabel = new Label("Okres sygnału:");
		Label liczbaPodstawowychSygnalowLabel = new Label("Liczba podstawowych sygnałów:");
		Label czestotliwoscProbkowaniaLabel = new Label("Częstotliwość próbkowania:");
		Label dlugoscBuforowLabel = new Label("Długośc buforów:");
		Label okresRaportowaniaLabel = new Label("Okres raportowania:");
		Label sygnalEmitowanyLabel = new Label("Sygnał emitowany:");
		Label sygnalOdbityLabel = new Label("Sygnał odbity:");
		Label korelacjaSygnalowLabel = new Label("Korelacja sygnałów:");
		Label diffLabel = new Label("odlglosc oryginalnego oraz różnica odległości powrotnego:");


		generate = new Button("Generuj");
		generate.setOnAction(this);


		liczbaPomiarowTextField = new TextField("10");
		predkoscRzeczywistaTextField = new TextField("10");
		predkoscAbstrakcyjnaTextField = new TextField("3000");
		okresSygnaluTextField = new TextField("1");
		liczbaPodstawowychSygnalowTextField = new TextField("2");
		czestotliwoscProbkowaniaTextField = new TextField("100");
		dlugoscBuforowTextField = new TextField("500");
		okresRaportowaniaTextField = new TextField("2");

		setFieldFormatters();


		HBox listBox = new HBox();

		emittedDistanceList = new ListView<>();
		returningDistanceList = new ListView<>();

		listBox.getChildren().addAll(emittedDistanceList, returningDistanceList);

		vPane.getChildren().addAll(liczbaPomiarowLabel, liczbaPomiarowTextField,
				predkoscRzeczywistaLabel, predkoscRzeczywistaTextField,
				predkoscAbstrakcyjnaLabel, predkoscAbstrakcyjnaTextField,
				okresSygnaluLabel, okresSygnaluTextField,
				liczbaPodstawowychSygnalowLabel, liczbaPodstawowychSygnalowTextField,
				czestotliwoscProbkowaniaLabel, czestotliwoscProbkowaniaTextField,
				dlugoscBuforowLabel, dlugoscBuforowTextField,
				okresRaportowaniaLabel, okresRaportowaniaTextField,
				generate,
				sygnalEmitowanyLabel, emittedPane.getView(),
				sygnalOdbityLabel, returningPane.getView(),
				korelacjaSygnalowLabel, correlationPane.getView(),
				diffLabel, listBox
		);

	}

	private void setFieldFormatters() {
		liczbaPomiarowTextField.setTextFormatter(getDecimalFormatter());
		predkoscRzeczywistaTextField.setTextFormatter(getDecimalFormatter());
		predkoscAbstrakcyjnaTextField.setTextFormatter(getDecimalFormatter());
		okresSygnaluTextField.setTextFormatter(getDecimalFormatter());
		liczbaPodstawowychSygnalowTextField.setTextFormatter(getDecimalFormatter());
		czestotliwoscProbkowaniaTextField.setTextFormatter(getDecimalFormatter());
		dlugoscBuforowTextField.setTextFormatter(getDecimalFormatter());
		okresRaportowaniaTextField.setTextFormatter(getDecimalFormatter());
	}

	private DistanceParam getDataFromView() {
		DistanceParam distanceParam = new DistanceParam();
		distanceParam.setCzestotliwoscProbkowania(Double.valueOf(czestotliwoscProbkowaniaTextField.getText()));
		distanceParam.setDlugoscBuforow(Double.valueOf(dlugoscBuforowTextField.getText()));
		distanceParam.setLiczbaPodstawowychSygnalow(Double.valueOf(liczbaPodstawowychSygnalowTextField.getText()));
		distanceParam.setLiczbaPomiarow(Double.valueOf(liczbaPomiarowTextField.getText()));
		distanceParam.setOkresRaportowania(Double.valueOf(okresRaportowaniaTextField.getText()));
		distanceParam.setOkresSygnalu(Double.valueOf(okresSygnaluTextField.getText()));
		distanceParam.setPredkoscAbstrakcyjna(Double.valueOf(predkoscAbstrakcyjnaTextField.getText()));
		distanceParam.setPredkoscRzeczywista(Double.valueOf(predkoscRzeczywistaTextField.getText()));

		return distanceParam;
	}

	public Pane getView() {
		return vPane;
	}

	@Override
	public void handle(Event event) {
		originalDistance.clear();
		returnDistance.clear();

		DistanceParam param = getDataFromView();

		//orginal dist
		for (double i = 0.0; i < (double) (param.getLiczbaPomiarow()) * param.getOkresRaportowania(); i += param.getOkresRaportowania()) {
			originalDistance.add(i * param.getPredkoscRzeczywista());
		}

		ArrayList<Double> amplitudes = new ArrayList();


		for (int i = 0; i < param.getOkresSygnalu(); i++) {
			amplitudes.add(Math.random() * 5.0 + 1.0);
		}

		double duration = param.getDlugoscBuforow() / param.getCzestotliwoscProbkowania();

		List<Double> emittedAmplitudes = new ArrayList<>();
		List<Double> returningAmplitudes = new ArrayList<>();
		List<Double> correlated = new ArrayList<>();

		for (double i = 0.0; i < (double) (param.getLiczbaPomiarow()) * param.getOkresRaportowania(); i += param.getOkresRaportowania()) {
			double currentDistance = i * param.getPredkoscRzeczywista();
			double propagationTime = 2 * currentDistance / param.getPredkoscAbstrakcyjna();

			//korelacja sygnałów
			DiscreteSignal emittedSignal = new DiscreteSignal();
			DiscreteSignal returningSignal = new DiscreteSignal();

			emittedAmplitudes = createDataset(amplitudes, param.getOkresSygnalu(), i - duration, duration, param.getCzestotliwoscProbkowania());
			returningAmplitudes = createDataset(amplitudes, param.getOkresSygnalu(), i - propagationTime, duration, param.getCzestotliwoscProbkowania());



			correlated = correlation(emittedAmplitudes, returningAmplitudes);

			returnDistance.add(calculateDistance(correlated, param.getPredkoscAbstrakcyjna(), param.getPredkoscAbstrakcyjna()));
		}
		emittedDistanceList.setItems(originalDistance);
		returningDistanceList.setItems(returnDistance);

		List<XYChart.Data<Double, Double>> emittedDataset = new ArrayList<>();
		List<XYChart.Data<Double, Double>> returningDataset = new ArrayList<>();
		List<XYChart.Data<Double, Double>> corelationDataset = new ArrayList<>();

		for(int i =0; i< emittedAmplitudes.size(); i++) {
			emittedDataset.add(new XYChart.Data(i *1.0, emittedAmplitudes.get(i)));
		}
		ContinuousSignal emittedSignal = new ContinuousSignal();
		emittedSignal.setDataset(emittedDataset);


		for(int i =0; i< returningAmplitudes.size(); i++) {
			returningDataset.add(new XYChart.Data(i *1.0, returningAmplitudes.get(i)));
		}
		ContinuousSignal returnirngSignal = new ContinuousSignal();
		returnirngSignal.setDataset(returningDataset);


		for(int i =0; i< correlated.size(); i++) {
			corelationDataset.add(new XYChart.Data(i *1.0, correlated.get(i)));
		}
		ContinuousSignal corelatedSignal = new ContinuousSignal();
		corelatedSignal.setDataset(corelationDataset);


		emittedPane.setChart(emittedSignal.getChart());
		returningPane.setChart(returnirngSignal.getChart());
		correlationPane.setChart(corelatedSignal.getChart());

	}


	private List<Double> createDataset(List<Double> amplitudes, double period, double startTime, double duration, double frequency) {
		List<Double> samples = new ArrayList<>();

		for (int i = 0; i < 1; i++) {
			List<Double> newSamples = new ArrayList<>();

			for (double j = startTime; j < (startTime + duration); j += 1 / frequency) {
				newSamples.add(amplitudes.get(i) * Math.sin(2 * Math.PI / period * j));

			}

			if (samples.size() != 0)
				samples = addSignals(newSamples, samples);
			else
				samples = newSamples;
		}

		return samples;

	}

	private List<Double> addSignals(List<Double> signal1, List<Double> signal2) {
		List<Double> result = new ArrayList<>();

		for (int i = 0; i < Math.max(signal1.size(), signal2.size()); i++) {

			result.add(signal1.get(i) + signal2.get(i));
		}

		return result;
	}


	private double calculateDistance(List<Double> correlation, double frequency, double abstractSpeed) {
		double maxSampleVal = -999999.0;
		int maxSampleIdx = 0;
		for (int i = 0; i < correlation.size() - 1; i++) {
			Double amplitudeVal = Math.abs(correlation.get(i));
			if (amplitudeVal > maxSampleVal) {
				maxSampleIdx = i;
				maxSampleVal = amplitudeVal;
			}

		}
		var delay = (maxSampleIdx - (correlation.size() / 2)) / frequency;
		return ((delay * abstractSpeed) / 2);
	}


	private List<Double> correlation(List<Double> firstDataset, List<Double> secondDataset) {
		List<Double> result = new ArrayList<>();

		for (int i = secondDataset.size() - 1; i >= (-1) * firstDataset.size(); i--) {
			double sum = 0.0;
			for (int j = 0; j < firstDataset.size(); j++) {
				if (!(i + j < 0 || i + j >= secondDataset.size())) {
					sum += firstDataset.get(j) * secondDataset.get(i + j);
				}
			}
			result.add(sum);
		}

		return result;
	}


}
