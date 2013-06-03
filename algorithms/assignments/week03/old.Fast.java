import java.util.Arrays;

public class Fast {

    private static class Pair
    {
        private Point p;
        private double s;

        // inner class constructor
        Pair(Point pt, double slope)
        {
            this.p = pt;
            this.s = slope;
        }
    }

    public static void main(String[] args)
    {
        // check for required command line argument
        if (args.length < 1) {
            throw new ArrayIndexOutOfBoundsException("Provide input file");
        }

        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);

        // input file
        String fin = args[0];
        In f = new In(fin);

        // first line is the number of (x, y) points
        int N = f.readInt();

        // set the minimum length of collinear lines to draw
        int MIN_LENGTH = 4;

        // array to store all points
        Point[] pts = new Point[N];

        // read in points data from file
        for (int i = 0; i < N; i++) {

            // read in a new (x, y) point
            int x = f.readInt();
            int y = f.readInt();

            // make a Point object
            Point p = new Point(x, y);

            // put Point object in pts array
            pts[i] = p;

            // draw the point
            p.draw();
        }

//        Arrays.sort(pts);

        ////=======================================////
        ////  ALL POINTS ARE NOW READ AND PLOTTED  ////
        ////=======================================////

        // Create array of Pairs to track drawn lines
        Pair[] pairs = new Pair[1];
        int P = 0; // track number of pairs added
        Pair pr = new Pair(pts[0], Double.NEGATIVE_INFINITY);
        Pair init = new Pair(pts[0], Double.NEGATIVE_INFINITY);
        pairs[0] = init;

        // loop over all points and compute slope arrays
        for (int i = 0; i < N; i++) {

            // i'th point is p
            Point p = pts[i];

            // make a copy of pts array for slope sorting
//            Point[] copy = Arrays.copyOfRange(pts, i, N);
//            Point[] copy = pts;
            Point[] copy = (Point[]) pts.clone();
//            Point[] copy = new Point[N];
//            System.arraycopy(pts, 0, copy, 0, N);

            // sort based on slopes
            Arrays.sort(copy, p.SLOPE_ORDER);

            // start count at 1 since p is already in line
            int count = 1;
            // intial slope should be -inf (p with itself)
            double s0 = p.slopeTo(copy[0]);
//            StdOut.println(count+"   "+s0+"  "+MIN_LENGTH+"  "+copy.length);

            // compute all of the slopes (inluding i with itself)
//            for (int j = 1; j < N-i; ++j) {
            for (int j = 1; j < N; ++j) {

                // compute slope from p to j
                double slope = p.slopeTo(copy[j]);

                // if slope equals s0, increment count
                if (slope == s0) count++;

                else {
                    // if long enough line detected, draw it
                    if (count >= MIN_LENGTH-1) {

                        // create subarray for collinear points (save spot for p)
                        Point[] col = Arrays.copyOfRange(copy, j-count-1, j);
                        // put p in collinear array
                        col[0] = p;
                        // sort collinear array
                        Arrays.sort(col);
                        // size of col
                        int C = col.length;

                        // create a pair
                        pr = new Pair(col[0], s0);

                        // check to see if pair in drawn pairs
                        int check = -1;
                        int w = 0;
                        while (check < 0 && w < pairs.length) {
                            if (pr.p == pairs[w].p && pr.s == pairs[w].s) check = 1;
                            w++;
                        }
//                        for (int k = 0; k < pairs.length; k++) {
//                            if (pr.p == pairs[k].p && pr.s == pairs[k].s) check = 1;
//                        }

                        // draw the line
                        if (check < 0) {
                            col[0].drawTo(col[C-1]);
//                            StdOut.println("------------ "+col[0]+" "+col[C-1]+" "+p);

                            // print the line end points to output
                            for (int k = 0; k < C-1; k++) {
                                StdOut.print(col[k]+" -> ");
                            }
                            StdOut.println(col[C-1]);

                            // add to pairs array //

                            // double array size if at max capacity
                            if (P == pairs.length) {

                                // create new array with given capacity
//                                Pair[] pcopy = (Pair[]) new Object[2*pairs.length];
                                Pair[] pcopy = new Pair[2*pairs.length];

                                // copy items to the new array
                                for (int k = 0; k < 2*pairs.length; k++) {
                                    if (k < P) pcopy[k] = pairs[k];
                                    else pcopy[k] = init;
                                }

                                // copy new array back over to pairs
                                pairs = pcopy;
                            }

                            // add new pair to pairs
                            pairs[P++] = pr;
                        }
                    }
                    // reset slope
                    s0 = slope;
                    // reset count
                    count = 1;
                }
            }
            // if long enough line detected at the end, be sure to draw it
            if (count >= MIN_LENGTH-1) {
//                p.drawTo(copy[N-i-1]);
//                StdOut.println("------------ "+p+" "+copy[N-i-1]);

                // create subarray for collinear points (save spot for p)
                Point[] col = Arrays.copyOfRange(copy, N-count-1, N);
                // put p in collinear array
                col[0] = p;
                // sort collinear array
                Arrays.sort(col);
                // size of col
                int C = col.length;

                // create a pair
                pr = new Pair(col[0], s0);

                // check to see if pair in drawn pairs
                int check = -1;
                int w = 0;
                while (check < 0 && w < pairs.length) {
                    if (pr.p == pairs[w].p && pr.s == pairs[w].s) check = 1;
                    w++;
                }
//                        for (int k = 0; k < pairs.length; k++) {
//                            if (pr.p == pairs[k].p && pr.s == pairs[k].s) check = 1;
//                        }

                // draw the line
                if (check < 0) {
                    col[0].drawTo(col[C-1]);

                    // print the line end points to output
                    for (int k = 0; k < C-1; k++) {
                        StdOut.print(col[k]+" -> ");
                    }
                    StdOut.println(col[C-1]);

                    // add to pairs array //

                    // double array size if at max capacity
                    if (P == pairs.length) {

                        // create new array with given capacity
                        Pair[] pcopy = new Pair[2*pairs.length];

                        // copy items to the new array
                        for (int k = 0; k < 2*pairs.length; k++) {
                            if (k < P) pcopy[k] = pairs[k];
                            else pcopy[k] = init;
                        }

                        // copy new array back over to pairs
                        pairs = pcopy;
                    }

                    // add new pair to pairs
                    pairs[P++] = pr;
                }
            }
        }
        StdDraw.show(0);
    }
}