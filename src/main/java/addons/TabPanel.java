package addons;

import javafx.scene.control.TabPane;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TabPanel extends TabPane {
    private WindowTab chart;
    private WindowTab histogram;
    private WindowTab parameters;

    public TabPanel(WindowTab chart, WindowTab histogram, WindowTab parameters) {
        super(chart, histogram, parameters);
        this.chart = chart;
        this.histogram = histogram;
        this.parameters = parameters;
    }

    public WindowTab getChart() {
        return chart;
    }

    public void setWindowTab(WindowTab chart) {
        this.chart = chart;
    }

    public WindowTab getParameters() {
        return parameters;
    }

    public void setParameters(WindowTab parameters) {
        this.parameters = parameters;
    }

    public WindowTab getHistogram() {
        return histogram;
    }

    public void setHistogram(WindowTab histogram) {
        this.histogram = histogram;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TabPanel tabPanel = (TabPanel) o;

        return new EqualsBuilder().append(chart, tabPanel.chart).append(parameters, tabPanel.parameters).append(histogram, tabPanel.histogram).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(chart).append(parameters).append(histogram).toHashCode();
    }

    @Override
    public String toString() {
        return "TabPanel{" +
                "chart=" + chart +
                ", parameters=" + parameters +
                ", histogram=" + histogram +
                '}';
    }
}
