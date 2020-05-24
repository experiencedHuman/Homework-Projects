package pgdp.minijvm;

public class FJump extends Instruction{
    private int i;

    public FJump(int i) {
        this.i = i;
    }

    @Override
    public void execute(Simulator simulator) {
        int value = simulator.getStack().pop();
        if (value == 0)
            simulator.setProgramCounter(i);
    }
}