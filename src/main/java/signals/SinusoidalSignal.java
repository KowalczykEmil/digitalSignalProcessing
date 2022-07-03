package signals;

public class SinusoidalSignal extends ContinousSignal {
    private final double amplitude;
    private final double term;

    public SinusoidalSignal(double rangeStart, double rangeLength, double amplitude, double term) {
        super(rangeStart, rangeLength);
        this.amplitude = amplitude;
        this.term = term;
    }

    @Override
    protected double value(double t) {
        return amplitude * Math.sin((2.0 * Math.PI / term) * (t - scopeStart));
    }
}
