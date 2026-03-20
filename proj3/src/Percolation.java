import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int n;
    private int numOpen;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufFull;
    private int top;
    private int bottom;


    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        n = N;
        grid = new boolean[n][n];
        numOpen = 0;
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufFull = new WeightedQuickUnionUF(n * n + 1);
        top = n * n;
        bottom = n * n + 1;
    }

    public void open(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            numOpen++;
            int curr = to1D(row, col);

            if (row == 0) {
                uf.union(curr, top);
                ufFull.union(curr, top);
            }
            if (row == n - 1) {
                uf.union(curr, bottom);
            }
            connect(row, col, row - 1, col);
            connect(row, col, row + 1, col);
            connect(row, col, row, col - 1);
            connect(row, col, row, col + 1);
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new IndexOutOfBoundsException();
        }
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new IndexOutOfBoundsException();
        }
        return isOpen(row, col) && ufFull.connected(to1D(row, col), top);
    }

    public int numberOfOpenSites() {
        return numOpen;
    }

    public boolean percolates() {
        return uf.connected(top, bottom);
    }

    private int to1D(int r, int c) {
        return r * n + c;
    }

    private void connect(int r1, int c1, int r2, int c2) {
        if (r2 >= 0 && r2 < n && c2 >= 0 && c2 < n) {
            if (isOpen(r2, c2)) {
                uf.union(to1D(r1, c1), to1D(r2, c2));
                ufFull.union(to1D(r1, c1), to1D(r2, c2));
            }
        }
    }

}
