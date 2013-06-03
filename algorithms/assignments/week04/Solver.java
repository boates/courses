public class Solver {

    private MinPQ<Node> pq, pqtwin;
    private Node pqmin, init, twinmin, twinit;

    // create Node object to append boards to PQ's
    private class Node implements Comparable<Node>
    {
        private Board board;
        private Node prev;
        private int moves;

        // constructor method
        private Node(Board b, Node p, int m)
        {
            board = b;
            prev  = p;
            moves = m;
        }

        // compareTo method
        public int compareTo(Node that)
        {
            // define priority functions as moves + manhattan
            int b1 = this.moves + this.board.manhattan();
            int b2 = that.moves + that.board.manhattan();

            // greater or less than cases for priority functions
            if (b1 > b2) return +1;
            if (b1 < b2) return -1;

            // break ties by check hamming priority function
//          if (b1 == b2 && this.board.hamming() > that.board.hamming()) return +1;
//          if (b1 == b2 && this.board.hamming() < that.board.hamming()) return -1;

            else return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        // initialize PQ's
        pq     = new MinPQ<Node>();
        pqtwin = new MinPQ<Node>();

        // store the initial Node for future comparisons
        this.init = new Node(initial, new Node(null, null, 0), 0);
        pq.insert(this.init);

        // create an initial twin Board for "twinitial" Node
        this.twinit = new Node(this.init.board.twin(), new Node(null, null, 0), 0);
        pqtwin.insert(this.twinit);

        // delete & retrieve the minimum board from the PQ
        pqmin   = pq.delMin();
        twinmin = pqtwin.delMin();

        // search for the goal board
        while (!pqmin.board.isGoal() && !twinmin.board.isGoal()) {

            // get pqmin's neighbors and add non-duplicates to the PQ
            for (Board b : pqmin.board.neighbors()) {
                // if not the same as the previous board (before pqmin)
                if (!b.equals(pqmin.prev.board)) {
                    // insert the neighbor Board into PQ
                    pq.insert(new Node(b, pqmin, pqmin.moves+1));
                }
            }
            // delete & retrieve the minimum board from the PQ
            pqmin = pq.delMin();

            //// DO THE SAME FOR THE TWIN PQ ////

            // get twinit's neighbors and add non-duplicates to its PQ
            for (Board b : twinmin.board.neighbors()) {
                // if not the same as the previous board (before twinmin)
                if (!b.equals(twinmin.prev.board)) {
                    // insert the neighbor Board into PQ
                    pqtwin.insert(new Node(b, twinmin, twinmin.moves+1));
                }
            }
            // delete & retrieve the minimum board from the PQ
            twinmin = pqtwin.delMin();
        }
        //// after the while loop, pqmin or pqtwin is the goal Board ////
    }

    // is the initial board solvable?
    public boolean isSolvable()
    {
        return this.pqmin.board.isGoal();
    }

    // min number of moves to solve initial board; -1 if no solution
    public int moves()
    {
        if (this.pqmin.board.isGoal()) return this.pqmin.moves;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution()
    {
        // return null if no solution
        if (!this.pqmin.board.isGoal()) return null;

        // initialize back-tracking Node
        Node n = this.pqmin;

        // create a stacks for the solution iterable
        Stack<Board> sol = new Stack<Board>();

        // loop over previous Boards in goal Board to initial
        while (n.board != null) {
            // enqueue the previous Board
            sol.push(n.board);
            // shift pqmin to its previous Board
            n = n.prev;
        }
        return sol;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}