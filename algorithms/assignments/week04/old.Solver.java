public class Solver {

//    private MinPQ<Board> pq, pqtwin;
//    private Board pqmin, init, twinmin, twinit;
    private MinPQ<Node> pq, pqtwin;
    private Node pqmin, init, twinmin, twinit;

    // create Node object to append boards to PQ's
    private class Node implements Comparable<Node>
    {
        private Board board;

        // constructor method
        private Node(Board b)
        {
            board = b;
        }

	    // compareTo method
	    private int compareTo(Node that)
	    {
	        // define priority functions as moves + manhattan
	        int b1 = board.moves + board.man;
	        int b2 = that.board.moves + that.board.man;

	        // greater or less than cases for priority functions
	        if (b1 > b2) return +1;
	        if (b1 < b2) return -1;

	        // break ties by check hamming priority function
	        if (b1 == b2 && board.ham > that.board.ham) return +1;
	        if (b1 == b2 && board.ham < that.board.ham) return -1;

	        else return 0;
	    }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        // initialize PQ's
//        pq     = new MinPQ<Board>();
//        pqtwin = new MinPQ<Board>();
        pq     = new MinPQ<Node>();
        pqtwin = new MinPQ<Node>();

        // store the initial Board for future comparisons
//        this.init = initial;
//        StdOut.println(initial);
        this.init = new Node(initial);
        StdOut.println(this.init.board);

        // create an initial twin Board "twinitial"
//        this.twinit = initial.twin();
//        StdOut.println(this.twinit);
        this.twinit = new Node(twinit);
        StdOut.println(this.twinit.board);

        // get initial's neighbors and add them to the PQ
        for (Board b : initial.neighbors()) {
            // insert the neighbor Board as a Node into PQ
//            pq.insert(b);
            pq.insert(new Node(b));
        }

        // get twinitial's neighbors and add them to its PQ
        for (Board b : this.twinit.neighbors()) {
            // insert the neighbor Board as a Node into twinitial PQ
//            pqtwin.insert(b);
            pqtwin.insert(new Node(b));
        }

        // delete & retrieve the minimum board from the PQ
        pqmin   = pq.delMin();
        twinmin = pqtwin.delMin();

        // search for the goal board
        while (!pqmin.board.isGoal() && !twinmin.board.isGoal()) {

            // get pqmin's neighbors and add non-duplicates to the PQ
            for (Board b : pqmin.board.neighbors()) {
                // if not the same as the previous board (before pqmin)
                if (!b.equals(pqmin.board.prev)) {
	                // insert the neighbor Board into PQ
                    pq.insert(new Node(b));
                }
            }
            // delete & retrieve the minimum board from the PQ
            pqmin = pq.delMin();

            //// DO THE SAME FOR THE TWIN PQ ////

            // get twinit's neighbors and add non-duplicates to its PQ
            for (Board b : twinmin.board.neighbors()) {
                // if not the same as the previous board (before twinmin)
                if (!b.equals(twinmin.board.prev)) {
	                // insert the neighbor Board into PQ
                    pqtwin.insert(new Node(b));
                }
            }
            // delete & retrieve the minimum board from the PQ
            twinmin = pqtwin.delMin();
        }

        //// after the while loop, pqmin or pqtwin is the goal Board ////

        // if the twin was the one with a solution, set pqmin to null
        if (!pqmin.board.isGoal()) pqmin.board = null;
    }

    // is the initial board solvable?
    public boolean isSolvable()
    {
        // only one of a board and its twin is solvable
        return this.pqmin.board.isGoal();
    }

    // min number of moves to solve initial board; -1 if no solution
    public int moves()
    {
        if (this.pqmin.board.isGoal()) return this.pqmin.board.moves;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution()
    {
        // return null if no solution
        if (this.twinmin.board.isGoal()) return null;

        // create a queue for the solution iterable
        Queue<Board> sol = new Queue<Board>();

        // append goal Board to solution
        sol.enqueue(pqmin.board);

        // loop over previous Boards in goal Board to initial
        while (!this.pqmin.board.equals(this.init.board)) {
	        // enqueue the previous Board
            sol.enqueue(this.pqmin.board.prev);
            // shift pqmin to its previous Board
            this.pqmin = this.pqmin.board.prev;
        }

        // append the initial Board to solution
        sol.enqueue(this.init.board);

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