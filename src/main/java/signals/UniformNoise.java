package signals;

import java.util.Random;

public class UniformNoise extends ContinousSignal{
    private final double amplitude;
    private final Random random;

    public UniformNoise(double rangeStart, double rangeLength, double amplitude) {
        super(rangeStart, rangeLength);
        this.amplitude = amplitude;
        this.random = new Random();
    }

    @Override
    protected double value(double t) {
        return (random.nextDouble() * 2.0 - 1.0) * amplitude;
    }
}
