package signals;

public abstract class DiscreteSignal extends Signal{
    final double scopeStart;
    final double length;
    final double frequency;

    public DiscreteSignal(double scopeStart, double length, double frequency) {
        super ((int) (length * frequency));
        this.scopeStart = scopeStart;
        this.length = length;
        this.frequency = frequency;
    }

    @Override
    public void generate() {

    }
    abstract protected double value (double t);
}
