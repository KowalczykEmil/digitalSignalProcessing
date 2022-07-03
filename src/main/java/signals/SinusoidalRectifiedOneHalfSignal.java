package signals;

public class SinusoidalRectifiedOneHalfSignal extends ContinousSignal{
    private final double amplitude;
    private final double term;

    public SinusoidalRectifiedOneHalfSignal(double rangeStart, double rangeLength,
                                            double amplitude, double term) {
        super(rangeStart, rangeLength);
        this.amplitude = amplitude;
        this.term = term;
    }

    @Override
    protected double value(double t) {
        return 0.5 * amplitude
                * (Math.sin((2.0 * Math.PI / term) * (t - scopeStart))
                + Math.abs(Math.sin((2.0 * Math.PI / term) * (t - scopeStart))));
    }
}
