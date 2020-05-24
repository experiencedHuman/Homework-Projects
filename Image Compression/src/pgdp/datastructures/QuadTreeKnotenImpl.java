package pgdp.datastructures;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class QuadTreeKnotenImpl implements QuadTreeKnoten {

    private int dim, color, realX, realY, size;
    private boolean leaf;
    private QuadTreeKnoten topLeft;
    private QuadTreeKnoten bottomLeft;
    private QuadTreeKnoten topRight;
    private QuadTreeKnoten bottomRight;
    private QuadTreeKnoten parent;
    private int[][] ownImage;
    private static int[][] realImage;

    public QuadTreeKnotenImpl(int dim, int color) {
        this.dim = dim;
        this.color = color;
        leaf = true;
    }

    public QuadTreeKnotenImpl(int dim, int color, int realX, int realY, boolean leaf, int[][] ownImage, int size) {
        this.dim = dim;
        this.color = color;
        this.realX = realX;
        this.realY = realY;
        this.leaf = leaf;
        this.ownImage = ownImage;
        this.size = size;
    }

    public QuadTreeKnotenImpl(int dim, int color, boolean leaf, int[][] ownImage, QuadTreeKnoten topLeft,
                              QuadTreeKnoten bottomLeft,
                              QuadTreeKnoten topRight,
                              QuadTreeKnoten bottomRight,
                              Square<Integer> xy,
                              int size) {
        this.dim = dim;
        this.color = color;
        this.leaf = leaf;
        this.ownImage = ownImage;
        this.topLeft = topLeft;
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.realX = xy.getX();
        this.realY = xy.getY();
        this.size = size;
    }


    public static QuadTreeKnoten buildFromIntArray(int[][] image) {
        int n = image.length;
        QuadTreeKnotenImpl.realImage = image;
        Square<Integer> square = new Square<>(n,0,0);
        QuadTreeKnoten root = findRec(square);
        if (root.isLeaf())
            return root;
        else {
            setParents(root);
            return root;
        }
    }

    private static QuadTreeKnoten findRec(Square<Integer> square) {
        if (square.dim == 1) {
            int x = square.X;
            int y = square.Y;
            return new QuadTreeKnotenImpl(square.dim, realImage[x][y], x, y,true, new int[][]{{realImage[x][y]}}, 1);
        }

        int x = square.X;
        int y = square.Y;

        QuadTreeKnoten topLeft = findRec(square.getTopLeft());
        QuadTreeKnoten bottomLeft = findRec(square.getBottomLeft());
        QuadTreeKnoten topRight = findRec(square.getTopRight());
        QuadTreeKnoten bottomRight = findRec(square.getBottomRight());


        int newLength = topLeft.toArray().length * 2;
        int[][] img = new int[newLength][newLength];

        int rows = newLength, cols = newLength;
        if (x >= newLength)
            rows = newLength + x;

        if (y >= newLength)
            cols = newLength + y;


        for (int r = x; r < rows; r++) {
            for (int c = y; c < cols; c++) {
                img[r-x][c-y] = realImage[r][c];
            }
        }

        int pixel = img[0][0];
        boolean bigLeafOf4 = Stream.of(topLeft,bottomLeft,topRight,bottomRight)
                .allMatch(leaf -> leaf.getColor() == pixel);

        Square<Integer> xy = new Square<>(topLeft.getRealX(),topLeft.getRealY());
        int totSize = topLeft.getSize() + topRight.getSize() + bottomLeft.getSize() + bottomRight.getSize();

        if (bigLeafOf4)
            return new QuadTreeKnotenImpl(topLeft.getDimension() * 2, pixel,true,
                    img, topLeft, bottomLeft, topRight, bottomRight, xy,1);
        return new QuadTreeKnotenImpl(topLeft.getDimension() * 2, -1,false,
                img, topLeft, bottomLeft, topRight, bottomRight, xy,1 + totSize);
    }



    private static void setParents(QuadTreeKnoten parent) {
        if (parent.isLeaf())
            return;
        QuadTreeKnoten topLeftChild = parent.getTopLeft();
        QuadTreeKnoten bottomLeftChild = parent.getBottomLeft();
        QuadTreeKnoten topRightChild = parent.getTopRight();
        QuadTreeKnoten bottomRightChild = parent.getBottomRight();

        topLeftChild.setParent(parent);
        bottomLeftChild.setParent(parent);
        topRightChild.setParent(parent);
        bottomRightChild.setParent(parent);

        setParents(topLeftChild);
        setParents(bottomLeftChild);
        setParents(topRightChild);
        setParents(bottomRightChild);
    }

    @Override
    public QuadTreeKnoten getTopLeft() {
        if (this.isLeaf())
            throw new NoSuchElementException("Leaf has no top left element!");
        return topLeft;
    }

    @Override
    public QuadTreeKnoten getTopRight() {
        if (this.isLeaf())
            throw new NoSuchElementException("Leaf has no top right element!");
        return topRight;
    }

    @Override
    public QuadTreeKnoten getBottomLeft() {
        if (this.isLeaf())
            throw new NoSuchElementException("Leaf has no bottom left element!");
        return bottomLeft;
    }

    @Override
    public QuadTreeKnoten getBottomRight() {
        if (this.isLeaf())
            throw new NoSuchElementException("Leaf has no bottom right element!");
        return bottomRight;
    }

    @Override
    public int getRelativeColor(int x, int y) {
        if (x >= ownImage.length || y >= ownImage.length || x < 0 || y < 0 || x > dim || y > dim)
            throw new IllegalArgumentException("no valid paramerters!");
        return this.toArray()[x][y];
    }

    @Override
    public void setRelativeColor(int x, int y, int color) {
        if (x >= ownImage.length || y >= ownImage.length || x < 0 || y < 0 || x > dim || y > dim)
            throw new IllegalArgumentException("no valid paramerters!");
        //recoloring with the same color changes nothing
        if (color == ownImage[x][y])
            return;

        //update original image and representation of this object of the image with new pixel
        ownImage[x][y] = color;
        realImage[realX + x][realY + y] = color;
        int n = ownImage.length;

        //case when leaf becomes inner node
        if (leaf || parent == null) {
            //since we are no longer an inner node and we split into more nodes we have no color anymore
            this.color = -1;
            updateImage(n);
            leaf = false;
            size += Stream.of(topLeft,bottomLeft,topRight,bottomRight)
                    .map(QuadTreeKnoten::getSize)
                    .reduce(Integer::sum)
                    .get();
            updateSizes(this);
            return;
        }

        //case when inner node becomes leaf
        boolean bigLeaf;
        updateImage(n);
        bigLeaf = Stream.of(topLeft, bottomLeft, topRight, bottomRight).allMatch(QuadTreeKnoten::isLeaf);

        if (bigLeaf) {
            leaf = true;
            size = 1;
            updateSizes(this);
            this.color = color;
        }

        //update parents until we reach root if necessary
        QuadTreeKnoten p = parent;
        while (p != null) {
            boolean update = Stream.of(p.getTopLeft(),p.getTopRight(),p.getBottomLeft(),p.getBottomRight())
                    .filter(QuadTreeKnoten::isLeaf)
                    .count() == 4;
            if (update) {
                p.makeLeaf();
                p.setColor(color);
                p = p.getParent();
                continue;
            }
            break;
        }


    }

    private void updateImage(int n) {
        Square<Integer> square = new Square<>(n,realX,realY);
        topLeft = findRec(square.getTopLeft());
        bottomLeft = findRec(square.getBottomLeft());
        topRight = findRec(square.getTopRight());
        bottomRight = findRec(square.getBottomRight());
    }

    private void updateSizes(QuadTreeKnoten node) {
        if (node.getParent() == null)
            return;
        QuadTreeKnoten parent = node.getParent();
        int newSize = Stream.of(parent.getTopLeft(),parent.getBottomLeft(),parent.getTopRight(),parent.getTopRight())
                .map(QuadTreeKnoten::getSize)
                .reduce(Integer::sum)
                .get();
        parent.updateSize(1 + newSize);
        updateSizes(parent);
    }

    @Override
    public int getDimension() {
        return dim;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isLeaf() {
        return leaf;
    }

    @Override
    public int[][] toArray() {
        return ownImage;
    }

    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }


    public int getRealX() {
        return realX;
    }

    public int getRealY() {
        return realY;
    }

    @Override
    public void makeLeaf() {
        leaf = true;
    }

    public void setParent(QuadTreeKnoten parent) {
        this.parent = parent;
    }

    @Override
    public QuadTreeKnoten getParent() {
        return parent;
    }

    @Override
    public void updateSize(int newSize) {
        this.size = newSize;
    }

}