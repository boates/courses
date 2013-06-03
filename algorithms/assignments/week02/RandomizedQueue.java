import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    private int N;     // size of the deque
    private Item[] q;  // array to hold items

    // construct an empty randomized queue
    public RandomizedQueue()
    {
        N = 0;
        q = (Item[]) new Object[1];
    }

    // if new item is null, do not allow
    private void validateItem(Item item)
    {
        if (item == null) {
            throw new NullPointerException("Cannot add null item");
        }
    }

    // is the queue empty?
    public boolean isEmpty()
    {
        return N == 0;
    }

    // return the number of items on the queue
    public int size()
    {
        return N;
    }

    // resize the q array
    private void resize(int capacity)
    {
        // create new array with given capacity
        Item[] copy = (Item[]) new Object[capacity];

        // copy items to the new array
        for (int i = 0; i < N; i++) {
            copy[i] = q[i];
        }

        // copy new array back over to q
        q = copy;
    }

    // add the item to the back
    public void enqueue(Item item)
    {
        // validate item is not null
        validateItem(item);

        // double array size if at max capacity
        if (N == q.length) { resize(2*q.length); }

        // place item in final spot and then increment N
        q[N++] = item;
    }

    // delete and return a random item
    public Item dequeue()
    {
        // if empty queue
        if (isEmpty()) throw new NoSuchElementException();

        // retrieve a random index from [0, N-1] = N items
        int r = StdRandom.uniform(N);

        // retrieve a random item from q
        Item item = q[r];

        // move element N-1 to r to avoid gaps of nulls
        // this works because the order doesn't matter
        q[r] = q[N-1];

        // decrement N, then avoid loitering at the N-1 slot
        q[--N] = null;

        // resize the array by half if q becomes quarter filled
        if (N > 0 && N == q.length/4) { resize(q.length/2); }

        // return the random item
        return item;
    }

    // return (but do not delete) a random item
    public Item sample()
    {
        // if empty queue
        if (isEmpty()) throw new NoSuchElementException();

        // return a random item from [0, N-1] = N items
        return q[ StdRandom.uniform(N) ];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() { return new ArrayIterator(); }

    private class ArrayIterator implements Iterator<Item>
    {

        // create new array with size N for shuffling
        private Item[] copy = (Item[]) new Object[N];

        // create an index to loop through shuffled array
        private int c = 0;

        // constructor
        public ArrayIterator()
        {
            // copy items to the new array
            for (int i = 0; i < N; i++) {
                copy[i] = q[i];
            }

            // shuffle the copied array
            StdRandom.shuffle(copy);
        }

        // there is a next item if c is less than N
        public boolean hasNext() { return c != N; }

        // not allowed
        public void remove() { throw new UnsupportedOperationException(); }

        // gets item, then moves to the next item
        public Item next()
        {
            if (!hasNext()) throw new NoSuchElementException();

            // retrieve copy[c] and then increment c
            return copy[c++];
        }
    }

    public static void main(String[] args) {
/*
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();

        StdOut.println("------"+rq.size());
        rq.enqueue(7);
        StdOut.println("------"+rq.size());
        rq.enqueue(1);
        StdOut.println("------"+rq.size());
        rq.enqueue(6);
        StdOut.println("------"+rq.size());
        rq.enqueue(3);
        StdOut.println("------"+rq.size());
        rq.enqueue(4);
        StdOut.println("------"+rq.size());

        for (int i : rq) { StdOut.println(i); }

        StdOut.println("ppppppppp");

        for (int i : rq) { for (int j : rq) { StdOut.println(i+" "+j); } }

        rq.dequeue();
        StdOut.println("------"+rq.size());
        rq.dequeue();
        StdOut.println("------"+rq.size());
        rq.dequeue();
        StdOut.println("------"+rq.size());
        rq.dequeue();
        StdOut.println("------"+rq.size());
        rq.dequeue();
*/
    }

}