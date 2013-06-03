public class Board {

    private final int N;
    private int[][] tiles;
    private int ham, man;

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks)
    {
        this.N = blocks.length;
        this.ham = 0; //this.hamming();
        this.man = 0; //this.manhattan();

        // deep copy of blocks
        int [][] tmp = new int[this.N][];
        for (int i = 0; i < this.N; i++) {
            tmp[i] = blocks[i].clone();
        }
        this.tiles = tmp;
    }

    // swap tile[i1][j1] with tile[i2][j2] as a new Board
    private Board swap(int i1, int j1, int i2, int j2)
    {
        // independent copy of this
        Board swapped = new Board(this.tiles);

        // perform the swap
        swapped.tiles[i1][j1] = this.tiles[i2][j2];
        swapped.tiles[i2][j2] = this.tiles[i1][j1];

        return swapped;
    }

    // board dimension N
    public int dimension()
    {
        return this.N;
    }

    // number of blocks out of place
    public int hamming()
    {
        // check if already computed
        if (this.ham != 0) return this.ham;

        // hamming priority value
        this.ham = 0;

        // loop through each item in tiles
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {

                // current tile
                int tile = this.tiles[i][j];

                // correct tile value for current indices
                int correct = this.N*i + j + 1;

                // if out of place, increment hamming function
                if (tile != 0 && tile != correct) {
                    this.ham++;
                }
            }
        }
        return this.ham;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan()
    {
        // check if already computed
        if (this.man != 0) return this.man;

        // hamming priority value
        this.man = 0;

        // loop through each item in tiles
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {

                // current tile
                int tile = this.tiles[i][j];

                // "correct" i index for tile
                int ci = (tile - 1) / N;

                // "correct" j index for tile
                int cj = (tile - 1) % this.N;

                // increment manhattan function by distance out of place
                if (tile != 0) {
                    if (ci - i > 0) this.man += ci - i;
                    else this.man += i - ci;
                    if (cj - j > 0) this.man += cj - j;
                    else this.man += j - cj;
                }
            }
        }
        return this.man;
    }

    // is this board the goal board?
    public boolean isGoal()
    {
        // loop over all tiles
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                int g = this.N*i +j + 1;
                if (i == this.N-1 && j == this.N-1) g = 0;
                if (this.tiles[i][j] != g) return false;
            }
        }
        return true;
    }

    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin()
    {
        // create independent copy
        Board cop = new Board(this.tiles);

        // point to twin tiles array
        int[][] t = cop.tiles;

        boolean swapped = false;
        int i = 0;
        int j = 0;

        // loop over i,j pairs in tiles until non-zero swap found
        while (!swapped && i < this.N) {
            j = 0;
            while (!swapped && j < this.N-1) {
                // try swapping t_ij with point to the right
                if (t[i][j] != 0 && t[i][j+1] != 0) {
                    // swap t_ij with point to the right
                    int tmp   = t[i][j];
                    t[i][j]   = t[i][j+1];
                    t[i][j+1] = tmp;
                    swapped   = true;
                }
                j++;
            }
            i++;
        }
        // return twin Board
        return cop;
    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        // trivial checks
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        // cast Object to Board
        Board that = (Board) y;

        // if not even same dimension
        if (this.N != that.N) return false;

        // define boolean check for tiles
        boolean check = true;

        // loop through tiles for this and that to check each item, break when false
        int i = 0;
        int j = 0;
        while (check && i < this.N) {
            j = 0;
            while (check && j < this.N) {
                if (this.tiles[i][j] != that.tiles[i][j]) { check = false; }
                j++;
            }
            i++;
        }
        return check;
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        // create a queue for the neighbors iterable
        Queue<Board> neighs = new Queue<Board>();

        // find the index of the zero (empty space)
        boolean found = false;
        int i0 = 0;
        int j0 = 0;
        int i = 0;
        int j = 0;
        while (!found && i < this.N) {
            j = 0;
            while (!found && j < this.N) {
                if (this.tiles[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                    found = true;
                }
                j++;
            }
            i++;
        }

        //// generate all of the neighbors (2, 3, or 4) ////

        // new board with zero swapped above, enqueue
        if (i0 != 0) neighs.enqueue(this.swap(i0, j0, i0-1, j0));

        // new board with zero swapped below, enqueue
        if (i0 != (this.N-1)) neighs.enqueue(this.swap(i0, j0, i0+1, j0));

        // new board with zero swapped left, enqueue
        if (j0 != 0) neighs.enqueue(this.swap(i0, j0, i0, j0-1));

        // new board with zero swapped right, enqueue
        if (j0 != (this.N-1)) neighs.enqueue(this.swap(i0, j0, i0, j0+1));

        return neighs;
    }

    // string representation of the board (in the output format specified below)
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args)
    {
        int[][] z = new int[3][3];
        z[0][0] = 1;
        z[0][1] = 2;
        z[0][2] = 3;
        z[1][0] = 4;
        z[1][1] = 0;
        z[1][2] = 6;
        z[2][0] = 7;
        z[2][1] = 8;
        z[2][2] = 5;
        Board a = new Board(z);

//        StdOut.println(a);

//        StdOut.println(a.twin());
//        for (Board b : a.neighbors()) StdOut.println(b);

//        StdOut.println(a.manhattan());

    }
}













