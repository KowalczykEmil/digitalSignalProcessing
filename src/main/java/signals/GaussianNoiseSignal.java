package signals;

import java.util.Random;

public class GaussianNoiseSignal extends ContinousSignal {

    private final Random random;
    private final double amplitude;


    public GaussianNoiseSignal(double scopeStart, double length, double amplitude) {
        super(scopeStart, length);
        this.random = new Random();
        this.amplitude = amplitude;
    }

    @Override
    protected double value(double t) {
        return (random.nextGaussian() * 2.0 - 1.0) * amplitude;
    }
}
