package signals;

import model.Action;
import model.Stats;

public class FinalSignal extends Signal {
    private final Signal fistSignal;
    private final Signal secondSignal;
    private final Action action;

    public FinalSignal(Signal firstSignal, Signal secondSignal, Action action) {
        super(firstSignal.getData().size());

        if (firstSignal.getData().size() != secondSignal.getData().size()) {
            throw new NotSameLengthException();
        }

        this.fistSignal = firstSignal;
        this.secondSignal = secondSignal;
        this.action = action;
    }

    @Override
    public void generate() {
        fistSignal.generate();
        secondSignal.generate();

        for (int i = 0; i < data.length; i++) {
            data[i] = new Stats(fistSignal.getData().get(i).getX(),
                    action.action(fistSignal.getData().get(i).getY(),
                            secondSignal.getData().get(i).getY()));
        }
    }

    public class NotSameLengthException extends RuntimeException {
    }
}
