package signals;

import java.util.Random;

public class ImpulseNoise extends DiscreteSignal{
    private final double amplitude;
    private final Random random;
    private final double probability;


    public ImpulseNoise(double rangeStart, double rangeLength, double sampleRate,
                        double amplitude, double probability) {
        super(rangeStart, rangeLength, sampleRate);
        this.amplitude = amplitude;
        this.random = new Random();
        this.probability = probability;
    }

    @Override
    protected double value(double t) {
        if (random.nextDouble() < probability) {
            return amplitude;
        } else {
            return 0.0;
        }
    }

}
