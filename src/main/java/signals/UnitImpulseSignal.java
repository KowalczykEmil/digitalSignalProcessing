package signals;

public class UnitImpulseSignal extends DiscreteSignal {
    private final double amplitude;
    private final int jumpSampleNumber;

    public UnitImpulseSignal(double rangeStart, double rangeLength, double sampleRate,
                             double amplitude, int jumpSampleNumber) {
        super(rangeStart, rangeLength, sampleRate);
        this.amplitude = amplitude;
        this.jumpSampleNumber = jumpSampleNumber;
    }

    @Override
    protected double value(double t) {
        double step = length / (data.length - 1);
        if (t == jumpSampleNumber * step) {
            return amplitude;
        } else {
            return 0.0;
        }
    }
}
