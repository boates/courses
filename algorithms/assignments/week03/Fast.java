import java.util.Arrays;

public class Fast {

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
        Point[] pts  = new Point[N];
        Point[] copy = new Point[N];

        // read in points data from file
        for (int i = 0; i < N; i++) {

            // read in a new (x, y) point
            int x = f.readInt();
            int y = f.readInt();

            // make a Point object
            Point p = new Point(x, y);

            // put Point object in pts array
            pts[i]  = p;
            copy[i] = p;

            // draw the point
            p.draw();
        }

        Arrays.sort(pts);

        ////=======================================////
        ////  ALL POINTS ARE NOW READ AND PLOTTED  ////
        ////=======================================////

        // loop over all points and compute slope arrays
        for (int i = 0; i < N; i++) {

            // i'th point is p
            Point p = pts[i];

            // sort based on slopes
            Arrays.sort(copy, p.SLOPE_ORDER);

            // start count at 1 since p is already in line
            int count = 1;
            // intial slope should be -inf (p with itself)
            double s0 = p.slopeTo(copy[0]);

            // compute all of the slopes (inluding i with itself)
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

                        // make sure the line is new
                        if (i == 0 || col[0].compareTo(pts[i-1]) > 0) {
                            // draw the line
                            col[0].drawTo(col[C-1]);

                            // print the line segment to output
                            for (int k = 0; k < C-1; k++) {
                                StdOut.print(col[k]+" -> ");
                            }
                            StdOut.println(col[C-1]);
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

                // create subarray for collinear points (save spot for p)
                int cl = copy.length;
                Point[] col = Arrays.copyOfRange(copy, cl-count-1, cl);
                // put p in collinear array
                col[0] = p;
                // sort collinear array
                Arrays.sort(col);
                // size of col
                int C = col.length;

                // make sure the line is new
                if (i == 0 || col[0].compareTo(pts[i-1]) > 0) {
                    // draw the line
                    col[0].drawTo(col[C-1]);

                    // print the line segment to output
                    for (int k = 0; k < C-1; k++) {
                        StdOut.print(col[k]+" -> ");
                    }
                    StdOut.println(col[C-1]);
                }
            }
        }
        StdDraw.show(0);
    }
}