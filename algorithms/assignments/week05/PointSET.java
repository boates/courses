public class PointSET {

    private SET<Point2D> s;

    // construct an empty set of points
    public PointSET()
    {
        this.s = new SET<Point2D>();  // the point set
    }

    // is the set empty?
    public boolean isEmpty()
    {
        return this.s.isEmpty();
    }

    // number of points in the set
    public int size()
    {
        return this.s.size();
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        if (!this.s.contains(p)) this.s.add(p);
    }

    // does the set contain the point p?
    public boolean contains(Point2D p)
    {
        return this.s.contains(p);
    }

    // draw all of the points to standard draw
    public void draw()
    {
        // drawing properties
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        // loop through all points in PointSET
        for (Point2D p : this.s) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect)
    {
        Queue<Point2D> q = new Queue<Point2D>();

        // loop over all points in PointSET
        for (Point2D p: this.s) {
            // if point in rect
            if (rect.contains(p)) q.enqueue(p);
        }
        return q;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p)
    {
        Point2D pmin = null;
        double  dmin = Double.POSITIVE_INFINITY;
        double  dnew = 0.0;

        for (Point2D pt : this.s) {
            dnew = p.distanceSquaredTo(pt);
            if (dnew < dmin) {
                dmin = dnew;
                pmin = pt;
            }
        }
        return pmin;
    }

    public static void main(String[] args)
    {
        PointSET set = new PointSET();
        Point2D p = new Point2D(0.5, 0.7);
        set.insert(p);
//        StdOut.println(p);
//        StdOut.println(set.nearest(new Point2D(0.20, 0.55)));


    }
}