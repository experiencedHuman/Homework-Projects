package pgdp.minijvm;

public class Store extends Instruction{
    private int i;

    public Store(int i) {
        this.i = i;
    }

    @Override
    public void execute(Simulator simulator) {
        int value = simulator.getStack().pop();
        simulator.getStack().setValueAtIndex(i, value);
    }
}