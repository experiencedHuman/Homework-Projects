package pgdp.datastructures;

public class Square<T> {
    private T x;
    private T y;

    protected Square(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    protected int Y, X, dim;

    protected Square(int dim, int X, int Y) {
        this.X = X;
        this.Y = Y;
        this.dim = dim;
    }

    public Square<Integer> getTopLeft() {
        return new Square<>(dim/2,X,Y);
    }

    public Square<Integer> getBottomLeft() {
        return new Square<>(dim/2,X + dim/2,Y);
    }

    public Square<Integer> getTopRight() {
        return new Square<>(dim/2,X,Y+dim/2);
    }

    public Square<Integer> getBottomRight() {
        return new Square<>(dim/2,X+dim/2,Y+dim/2);
    }
}