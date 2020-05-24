package pgdp.minijvm;

public class Load extends Instruction{
    private int i;

    public Load(int i) {
        this.i = i;
    }

    @Override
    public void execute(Simulator simulator) {
        int value = simulator.getStack().getValueAtIndex(i);
        simulator.getStack().push(value);
    }
}