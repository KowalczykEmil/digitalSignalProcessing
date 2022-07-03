package signals;

public abstract class ContinousSignal extends DiscreteSignal{
    private static final int AMOUNT_FREQUENCY = 1_000_000;

    public ContinousSignal(double scopeStart, double length) {
        super(scopeStart, length, AMOUNT_FREQUENCY / length);
    }
}
