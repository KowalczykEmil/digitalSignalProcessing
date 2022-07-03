package signals;

public class RectangularSymmetricSignal extends ContinousSignal {

    private final double amplitude;
    private final double term;
    private final double fulfillment;

    public RectangularSymmetricSignal(double rangeStart, double rangeLength, double amplitude,
                                      double term, double fulfillment) {
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
            return -amplitude;
        }
    }

}
