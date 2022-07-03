package signals;

import model.Scope;
import model.Stats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

    public abstract class Signal implements Serializable {

        protected final Stats[] data;

        public Signal(int length) {
            this.data = new Stats[length];
        }

        public abstract void generate();

        public List<Stats> getData() {
            return Arrays.asList(data);
        }

        public List<Scope> generateHistogram(int numberOfRanges) {
            final double min = Arrays.asList(data).stream()
                    .mapToDouble(data -> data.getY()).min().getAsDouble();
            final double max = Arrays.asList(data).stream()
                    .mapToDouble(data -> data.getY()).max().getAsDouble();
            final List<Scope> scopes = new ArrayList<>();
            IntStream.range(0, numberOfRanges).forEach(i -> {
                double start = min + (max - min) / numberOfRanges * i;
                double end = min + (max - min) / numberOfRanges * (i + 1);
                int amount = (int) Arrays.asList(data).stream()
                        .filter(data -> data.getY() >= start && data.getY() <= end)
                        .count();
                scopes.add(new Scope(amount, start, end));
            });
            return scopes;
        }

        public double meanValue() {
            double sum = 0;
            for (int i = 0; i < data.length; i++) {
                sum += data[i].getY();
            }
            return sum / data.length;
        }

        public double absMeanValue() {
            double sum = 0;
            for (int i = 0; i < data.length; i++) {
                sum += Math.abs(data[i].getY());
            }
            return sum / data.length;
        }

        public double rmsValue() {
            return Math.sqrt(meanPowerValue());
        }

        public double varianceValue() {
            double mean = meanValue();
            double sum = 0;
            for (int i = 0; i < data.length; i++) {
                sum += Math.pow(data[i].getY() - mean, 2.0);
            }
            return sum / data.length;
        }

        public double meanPowerValue() {
            double sum = 0;
            for (int i = 0; i < data.length; i++) {
                sum += Math.pow(data[i].getY(), 2.0);
            }
            return sum / data.length;
        }
    }
