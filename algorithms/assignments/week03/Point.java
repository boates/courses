import java.util.Comparator;
import java.util.Arrays;

public class Point implements Comparable<Point> {

    // compare points by slope to this point
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();

    private class SlopeOrder implements Comparator<Point>
    {
        public int compare(Point q1, Point q2)
        {
            if      (slopeTo(q1) > slopeTo(q2)) return +1;
            else if (slopeTo(q1) < slopeTo(q2)) return -1;
            else return 0;
        }
    }

    // x & y coordinates
    private final int x, y;

    // construct the point (x, y)
    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    // draw this point
    public void draw()
    {
        StdDraw.point(x, y);
    }

    // draw the line segment from this point to that point
    public void drawTo(Point that)
    {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // string representation
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    // is this point lexicographically smaller than that point?
    public int compareTo(Point that)
    {
        // +1 if this > that, -1 if this < that, 0 if this == that
        // compared by y coord, x coord breaks the tie
        if (this.y > that.y) return +1;
        else if (this.y < that.y) return -1;
        else if (this.y == that.y && this.x > that.x) return +1;
        else if (this.y == that.y && this.x < that.x) return -1;
        else return 0;
    }

    // the slope between this point and that point
    public double slopeTo(Point that)
    {
        // compute slope as rise over run 
        double rise = that.y - this.y;
        double run  = that.x - this.x;

        ////  "treat the slope of a vertical line segment as positive  ////
        ////  infinity; treat the slope of a degenerate line segment   ////
        ////  (between a point and itself) as negative infinity"       ////
        if (run == 0 && rise != 0) return Double.POSITIVE_INFINITY;
        if (run == 0 && rise == 0) return Double.NEGATIVE_INFINITY;

        // "treat the slope of a horizontal line segment as positive zero"
        if (rise == 0 && run != 0) { return 0.0; }

        // return slope as rise over run
        return rise / run;
    }

    // main function
    public static void main(String[] args) {

        Point p, q, v, u, w, z;
        p = new Point(1, 2);
        q = new Point(3, 4);
        v = new Point(5, -5);
        u = new Point(10, 2);
        z = new Point(1, 6);
        w = p;

        if (q.compareTo(v) > 0) StdOut.println(" yup ");
        double slope = u.slopeTo(p);
        StdOut.println(slope);

        Point[] pts = {p, v, q, v, u, w, z};
        for (int i = 0; i < pts.length; ++i) {
            StdOut.println(pts[i]+" "+p.slopeTo(pts[i]));
        }
        StdOut.println("========");

        Arrays.sort(pts, p.SLOPE_ORDER);
//        Arrays.sort(pts);

        for (int i = 0; i < pts.length; ++i) {
            StdOut.println(pts[i]+" "+p.slopeTo(pts[i]));
        }
        StdOut.println("========");


        Point[] pnew = Arrays.copyOfRange(pts, 2, pts.length);
        for (int i = 0; i < pnew.length; ++i) {
            StdOut.println(pnew[i]);
        }


    }
}








