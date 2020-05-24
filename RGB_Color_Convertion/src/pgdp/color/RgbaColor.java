package pgdp.color;

public class RgbaColor extends RgbColor{
    final private int alpha;

    public RgbaColor(int bitDepth, int red, int green, int blue, int alpha) {
        super(bitDepth,red,green,blue);
        if (!isValid(alpha))
            ExceptionUtil.unsupportedOperation("alpha is not maximal: "+alpha);
        this.alpha = alpha;
    }

    public int getAlpha() {
        return alpha;
    }

    @Override
    public RgbColor8Bit toRgbColor8Bit() {
        if (this.getBitDepth() == 31 && alpha != 2147483647)
            ExceptionUtil.unsupportedOperation("alpha is not maximal");
        if (alpha != (1<<this.getBitDepth()) - 1)
            ExceptionUtil.unsupportedOperation("alpha is not maximal");

        return super.toRgbColor8Bit();
    }

    private boolean isValid(int color) {
        if (this.getBitDepth() == 31)
            return color >= 0 && color <= 2147483647;
        else
            return color >= 0 && color <= (IntMath.powerOfTwo(this.getBitDepth()) - 1);
    }
}