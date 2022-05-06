package model;

public class Scope {
    private final int amount;
    private final double start;
    private final double end;

    public Scope(int amount, double start, double end) {
        this.amount = amount;
        this.start = start;
        this.end = end;
    }

    public int getAmount() {
        return amount;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }
}
