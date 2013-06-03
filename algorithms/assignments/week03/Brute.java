import java.util.Arrays;

public class Brute {

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

        ////  ALL POINTS ARE NOW READ AND PLOTTED  ////

        // loop over all 4-tuples
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {

                // get the slope from i to j
                double sij = pts[i].slopeTo(pts[j]);

                for (int k = 0; k < j; k++) {

                    // get the slope from i to k
                    double sik = pts[i].slopeTo(pts[k]);

                    // check if the first 3 points are collinear
                    if (sij == sik) {

                        // check for a fourth collinear point
                        for (int l = 0; l < k; l++) {

                            // get the slope from i to l
                            double sil = pts[i].slopeTo(pts[l]);

                            // check if collinear
                            if (sil == sij) {

                                // create array for the 4 collinear points
                                Point[] four = {pts[i], pts[j], pts[k], pts[l]};

                                // sort the four points
                                Arrays.sort(four);

                                // draw the line from start to end
                                four[0].drawTo(four[3]);

                                // print the line end points to output
                                StdOut.print(four[0]+" -> ");
                                StdOut.print(four[1]+" -> ");
                                StdOut.print(four[2]+" -> ");
                                StdOut.println(four[3]);
                            }
                        }
                    }
                }
            }
        }
        StdDraw.show(0);
    }
}