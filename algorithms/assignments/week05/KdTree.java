public class KdTree {

    private Node root;
    private int N;

    private static class Node
    {
        private Point2D p;    // the point
        private RectHV rect;  // node's rectangle
        private Node left;    // the left/bottom subtree
        private Node right;   // the right/top subtree
        private boolean isV;

        private Node(Point2D p, boolean isV, RectHV rect)
        {
            this.p    = p;
            this.isV  = isV;
            this.rect = rect;
        }
    }

    // construct an empty set of points
    public KdTree()
    {
        N = 0;
    }

    // is the set empty?
    public boolean isEmpty()
    {
        return size() == 0;
    }

    // number of points in the set
    public int size()
    {
        return N;
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        if (!contains(p)) {
            Node fake = new Node(new Point2D(0,0), true, new RectHV(0, 0, 1, 1));
            root = insert(root, true, fake, p, 0);
            N++;
        }
    }

    private Node insert(Node n, boolean isV, Node u, Point2D p, int i)
    {
        // check for null node
        if (n == null) {

            // grab previous node's rectangle dimensions
            double xmin = u.rect.xmin();
            double ymin = u.rect.ymin();
            double xmax = u.rect.xmax();
            double ymax = u.rect.ymax();

            // build rectangle depending on the previous step (4 possible cases)
            RectHV r;
            if (i == 1) { r = new RectHV(xmin, ymin, u.p.x(), ymax); }
            else if (i == 2) { r = new RectHV(u.p.x(), ymin, xmax, ymax); }
            else if (i == 3) { r = new RectHV(xmin, ymin, xmax, u.p.y()); }
            else if (i == 4) { r = new RectHV(xmin, u.p.y(), xmax, ymax); }
            else { r = new RectHV(0, 0, 1, 1); }

            return new Node(p, isV, r);
        }

        // if current Node is "vertical"
        if (n.isV) {
            // get the node and point x values
            double nx = n.p.x();
            double px = p.x();
            // compare x coords
            if (px < nx) n.left  = insert(n.left, !n.isV, n, p, 1); // case 1
            else if (px >= nx) n.right = insert(n.right, !n.isV, n, p, 2); // case 2
        }
        // if current Node is "horizontal"
        else if (!n.isV) {
            // get the node and point y values
            double ny = n.p.y();
            double py = p.y();
            // compare y coords
            if (py < ny) n.left  = insert(n.left, !n.isV, n, p, 3); // case 3
            else if (py >= ny) n.right = insert(n.right, !n.isV, n, p, 4); // case 4
        }
        return n;
    }

    // does the set contain the point p?
    public boolean contains(Point2D p)
    {
        return contains(root, p);
    }

    // check if key exists
    private boolean contains(Node n, Point2D p)
    {
        // if node is null
        if (n == null) return false;

        // grab the node's and point's x and y values
        double nx = n.p.x();
        double ny = n.p.y();
        double px = p.x();
        double py = p.y();

        // if node is found
        if (px == nx && py == ny) return true;

        // if current Node is "vertical"
        else if (n.isV) {
            // compare x coords
            if (px < nx)       return contains(n.left,  p);
            else if (px >= nx) return contains(n.right, p);
        }
        // if current Node is "horizontal"
        else if (!n.isV) {
            // compare y coords
            if (py < ny)       return contains(n.left,  p);
            else if (py >= ny) return contains(n.right, p);
        }
        return false;
    }

    // draw all of the points to standard draw
    public void draw()
    {
        draw(root);
    }

    private void draw(Node n)
    {
        // don't draw null nodes
        if (n == null) return;

        // draw the point's line first
        StdDraw.setPenRadius();
        if (n.isV) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        }
        else if (!n.isV) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }
        // draw the point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(n.p.x(), n.p.y());

        // draw the left and right nodes
        draw(n.left);
        draw(n.right);
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect)
    {
        // create an empty queue for points
        Queue<Point2D> q = new Queue<Point2D>();

        // if KdTree is emtpy, return empty queue
        if (this.isEmpty()) return q;

        return checkNode(q, root, rect);
    }

    // recursively check for rectangle intersections
    private Queue<Point2D> checkNode(Queue<Point2D> q, Node n, RectHV r)
    {
        // if node is null
        if (n == null) return q;
        // if node's rectangles intersects with query rectangle
        else if (n.rect.intersects(r)) {
            // enqueue that node's point if inside r
            if (r.contains(n.p)) q.enqueue(n.p);
            // check both children
            q = checkNode(q, n.left,  r);
            q = checkNode(q, n.right, r);
        }
        return q;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p)
    {
        if (this.isEmpty()) return null;
        else return nearest(root, root, p).p;
    }

    private Node nearest(Node n, Node nmin, Point2D pref)
    {
        // avoid null nodes
        if (n == null) return nmin;

        // update nmin if necessary
        if (pref.distanceSquaredTo(n.p) < pref.distanceSquaredTo(nmin.p)) nmin = n;

        // if current Node is "vertical"
        if (n.isV) {
            // compare x coords
            if (pref.x() < n.p.x()) {
                nmin = nearest(n.left, nmin, pref);
                if (n.right != null) {
                    double dmin = pref.distanceSquaredTo(nmin.p);
                    double rr2p = n.right.rect.distanceSquaredTo(pref);
                    if (rr2p < dmin) nmin = nearest(n.right, nmin, pref);
                }
            }
            else if (pref.x() >= n.p.x()) {
                nmin = nearest(n.right, nmin, pref);
                if (n.left != null) {
                    double dmin = pref.distanceSquaredTo(nmin.p);
                    double lr2p = n.left.rect.distanceSquaredTo(pref);
                    if (lr2p < dmin) nmin = nearest(n.left, nmin, pref);
                }
            }
        }
        // if current node is "horizontal"
        else if (!n.isV) {
            // compare y coords
            if (pref.y() < n.p.y()) {
                nmin = nearest(n.left, nmin, pref);
                if (n.right != null) {
                    double dmin = pref.distanceSquaredTo(nmin.p);
                    double rr2p = n.right.rect.distanceSquaredTo(pref);
                    if (rr2p < dmin) nmin = nearest(n.right, nmin, pref);
                }
            }
            else if (pref.y() >= n.p.y()) {
                nmin = nearest(n.right, nmin, pref);
                if (n.left != null) {
                    double dmin = pref.distanceSquaredTo(nmin.p);
                    double lr2p = n.left.rect.distanceSquaredTo(pref);
                    if (lr2p < dmin) nmin = nearest(n.left, nmin, pref);
                }
            }
        }
        return nmin;
    }

    public static void main(String[] args)
    {
        KdTree kd = new KdTree();

        kd.insert(new Point2D(0.52, 0.72));
        kd.insert(new Point2D(0.44, 0.67));
        kd.insert(new Point2D(0.70, 0.12));
        kd.insert(new Point2D(0.81, 0.06));

//        StdOut.println(kd.nearest(new Point2D(0.70,0.01)));

    }
}