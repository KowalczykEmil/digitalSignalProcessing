package signals;

public class TriangularSignal extends ContinousSignal{
    private final double amplitude;
    private final double term;
    private final double fulfillment;

    public TriangularSignal(double rangeStart, double rangeLength,
                            double amplitude, double term, double fulfillment) {
        super(rangeStart, rangeLength);
        this.amplitude = amplitude;
        this.term = term;
        this.fulfillment = fulfillment;
    }

    @Override
    protected double value(double t) {
        double termPosition = ((t - scopeStart) / term) - Math.floor((t - scopeStart) / term);
        if (termPosition < fulfillment) {
            return termPosition / fulfillment * amplitude;
        } else {
            return (1 - (termPosition - fulfillment) / (1 - fulfillment)) * amplitude;
        }
    }
}
