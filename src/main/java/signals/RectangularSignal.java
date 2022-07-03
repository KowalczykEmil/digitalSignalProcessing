package signals;

public class RectangularSignal extends ContinousSignal {
    private final double amplitude;
    private final double term;
    private final double fulfillment;

    public RectangularSignal(double rangeStart, double rangeLength,
                             double amplitude, double term, double fulfillment) {
        super(rangeStart, rangeLength);
        this.amplitude = amplitude;
        this.term = term;
        this.fulfillment = fulfillment;
    }

    @Override
    protected double value(double t) {
        if (((t - scopeStart) / term) - Math.floor((t - scopeStart) / term) < fulfillment) {
            return amplitude;
        } else {
            return 0.0;
        }
    }

}
