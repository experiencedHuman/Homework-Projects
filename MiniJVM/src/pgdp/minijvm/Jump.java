package pgdp.minijvm;

public class Jump extends Instruction{
    private int i;

    public Jump(int i) {
        this.i = i;
    }

    @Override
    public void execute(Simulator simulator) {
        simulator.setProgramCounter(i);
    }
}