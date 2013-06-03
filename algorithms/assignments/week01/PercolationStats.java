public class PercolationStats {

    private int N;  // dimensions for percolation grid
    private int T;  // number of simulations
    private double[] thresholds;  // array of percolation thresholds

    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {

        // throw excpetion if N or T <= 0
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("not allowed N <= 0 or T <= 0");
        }

        // set self parameters
        this.N = N;
        this.T = T;
        this.thresholds = new double[T];

        // total number of sites is N^2
        double nSites = N*N;

        // loop over number of simulations T
        for (int s = 0; s < T; s++) {

            //////////////////////////////////////////
            // perform a NxN percolation experiment //
            //////////////////////////////////////////

            // start with a new percolation object
            Percolation p = new Percolation(N);

            // track the number of "openings"
            int opened = 0;

            // while it is *not* percolating
            while (!p.percolates()) {

                // get a random integer indices between 1->N
                int i = StdRandom.uniform(1, N+1);
                int j = StdRandom.uniform(1, N+1);

                // increment number of openings only if (i,j) not already open
                if (!p.isOpen(i, j)) { opened += 1; }

                // open random site (i, j)
                p.open(i, j);
            }

            // once the loop ends, p percolates, save the threshold
            this.thresholds[s] = opened / nSites;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.thresholds);
    }

    // test client, described below
    public static void main(String[] args) {

        // check for required command line arguments
        if (args.length < 2) {
            throw new ArrayIndexOutOfBoundsException("Provide N & T");
        }

        // set N & T
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        // create percolationStats object
        PercolationStats ps = new PercolationStats(N, T);

        // compute quantities to report
        double mean   = ps.mean();
        double stddev = ps.stddev();
        double c95m = mean - 1.96 * stddev / Math.sqrt(T); // mean plus conf95
        double c95p = mean + 1.96 * stddev / Math.sqrt(T); // mean minus conf95

        // print the results
        StdOut.println("mean                    = "+mean);
        StdOut.println("stddev                  = "+stddev);
        StdOut.println("95% confidence interval = "+c95m+", "+c95p);
    }
}