public class Percolation {

    private int N;                     // grid length
    private boolean[][] grid;          // grid array
    private WeightedQuickUnionUF net;  // percolation network
    private WeightedQuickUnionUF abw;  // 2nd percolation network to avoid backwash
    private int top;                   // vitual top node index
    private int bot;                   // virtual bottom node index

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {

        // throw excpetion if N <= 0
        if (N <= 0) {
            throw new IllegalArgumentException("N must be a positive integer");
        }

        this.N = N;
        this.grid = new boolean[N+1][N+1]; // initialized to false (blocked)
        this.net = new WeightedQuickUnionUF(N*N+2);
        this.abw = new WeightedQuickUnionUF(N*N+2);
        this.top = N*N;   /// first value beyond the end of UF object
        this.bot = N*N+1; // second value beyond the end of UF object
    }
    
    // check to see if i and j are valid indices
    private void validateIndices(int i, int j) {
        if (i <= 0 || i > this.N) { 
            throw new IndexOutOfBoundsException("row index i out of bounds");
        }
        if (j <= 0 || j > this.N) { 
            throw new IndexOutOfBoundsException("column index j out of bounds");
        }
    }

    // convert from (i,j) pair to a single UF object index
    private int convertIndices(int i, int j) {
        return (i-1)*this.N + (j-1);
    }

    // open site (row i, column j) if it is not already
    public void open(int i, int j) {
        
        // make sure i and j are valid indices
        validateIndices(i, j);

        // open site (i,j)
        if (!isOpen(i, j)) {

            // set grid point to true (open)
            this.grid[i][j] = true;

            ////////////////////////
            //// PERFORM UNIONS ////
            ////////////////////////

            // translate to UF object index
            int u = convertIndices(i, j);

            // left side (if there is one)
            if (j > 1) { if (isOpen(i, j-1)) {
                this.net.union(u, convertIndices(i, j-1));
                this.abw.union(u, convertIndices(i, j-1));
            } }

            // right side (if there is one)
            if (j < N) { if (isOpen(i, j+1)) {
                this.net.union(u, convertIndices(i, j+1));
                this.abw.union(u, convertIndices(i, j+1));
            } }

            // top side (virtual if necessary)
            if (i > 1) { if (isOpen(i-1, j)) { 
                this.net.union(u, convertIndices(i-1, j));
                this.abw.union(u, convertIndices(i-1, j));
            } }
            if (i == 1) {
                this.net.union(u, this.top);
                this.abw.union(u, this.top);
            }

            // bottom side (virtual if necessary)
            if (i < N) { if (isOpen(i+1, j)) {
                this.net.union(u, convertIndices(i+1, j));
                this.abw.union(u, convertIndices(i+1, j));
            } }
            if (i == N) { this.net.union(u, this.bot); }
        }
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {

        // make sure i and j are valid indices
        validateIndices(i, j);

        // translate to UF object index
        int u = convertIndices(i, j);

        // open if site = true
        return this.grid[i][j];
    }
    
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {

        // make sure i and j are valid indices
        validateIndices(i, j);

        // translate to UF object index
        int u = convertIndices(i, j);

        // full if connected to the top (i.e. "full" of water) (avoiding backwash)
        return this.abw.connected(u, this.top);
    }

    // does the system percolate?
    public boolean percolates() {
        // percolates if top is connected to bottom
        return this.net.connected(this.top, this.bot);
    }

/*
    public static void main(String[] args) {
        int N = 10;
    }
*/
}
