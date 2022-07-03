package model.signal;

import javafx.scene.chart.*;
import org.apache.commons.math3.util.Precision;

public class DiscreteSignal extends AbstractSignal {

    protected XYChart getNewChart(NumberAxis xAxis, NumberAxis yAxis) {
        ScatterChart chart = new ScatterChart<Number, Number>(xAxis, yAxis);
        return chart;
    }

}
