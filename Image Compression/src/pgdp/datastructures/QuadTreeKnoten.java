package pgdp.datastructures;

public interface QuadTreeKnoten {
    QuadTreeKnoten getTopLeft();
    QuadTreeKnoten getTopRight();
    QuadTreeKnoten getBottomLeft();
    QuadTreeKnoten getBottomRight();

    int getRelativeColor(int x, int y);
    void setRelativeColor(int x, int y, int color);

    int getDimension();
    int getSize();
    boolean isLeaf();
    int[][] toArray();

    int getColor();
    void setColor(int color);
    void setParent(QuadTreeKnoten parent);
    QuadTreeKnoten getParent();
    int getRealX();
    int getRealY();
    void makeLeaf();
    void updateSize(int size);

    default double getCompressionRatio() {
        double nodes = this.getSize();
        double pixels = Math.pow(this.getDimension(), 2);
        return (nodes / pixels);
    }
}