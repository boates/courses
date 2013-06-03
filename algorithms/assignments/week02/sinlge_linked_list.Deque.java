import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int N;       // size of the deque
    private Node first;  // first node in deque
    private Node last;   // last node in deque

    // private class for nodes in linked-list implementation
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    // construct an empty deque
    public Deque() {
        N = 0;
//        first = null;
//        last = null;
//        first.item = null;
//        first.next = null;
//        last.item  = null;
//        last.next  = null;
    }

    // if new item is null, do not allow
    private void validateItem(Item item)
    {
        if (item == null) {
            throw new NullPointerException("Cannot add null item");
        }
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return N;
    }

    // insert the item at the front
    public void addFirst(Item item)
    {
        // validate item is not null to begin
        validateItem(item);

        // if empty deque
        if (isEmpty()) {
            first.item = item;
            last = first;
        } else {
            // save a link to the old first
            Node oldfirst = first;

            // create a new node for the beginning
            first = new Node();

            // set the intance variables in the new node
            first.item = item;
            first.next = oldfirst;
        }
        // increment size of deque
        N++;
    }

    // insert the item at the end
    public void addLast(Item item)
    {
        // validate item is not null to begin
        validateItem(item);

        // if empty deque
        if (isEmpty()) {
            last.item = item;
            first = last;
        } else {
            // save a link to the old last
            Node oldlast = last;

            // create a new node for the end
            last = new Node();

            // set the instance variables in the new node
            last.item = item;
            last.next = null;

            // reset oldlast to point to last
            oldlast.next = last;
        }
        // increment size of deque
        N++;
    }

    // delete and return the item at the front
    public Item removeFirst()
    {
        // if empty deque
        if (isEmpty()) throw new NoSuchElementException();

        // capture the current first item
        Item item = first.item;

        // first is now the old first's next
        first = first.next;

        // avoid loitering
        if (isEmpty()) last = null;

        // decrement the size of the deque
        N--;

        return item;
    }

    // delete and return the item at the end
    public Item removeLast() 
    {
        // if empty deque
        if (isEmpty()) throw new NoSuchElementException();

        // capture the current first item
        Item item = last.item;

        // first is now the old first's next
        last = first.next;

        // avoid loitering
        if (isEmpty()) last = null;

        // decrement the size of the deque
        N--;

        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() { return new ListIterator(); }

    private class ListIterator implements Iterator<Item> {

        private Node current = first;

        // there is a next if this node isn't null
        public boolean hasNext() { return current != null; }

        // not allowed
        public void remove() { throw new UnsupportedOperationException(); }

        // gets current item, then moves current along to next and returns item
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }


    public static void main(String[] args) {
        Deque deck = new Deque();

//        deck.addFirst(3);
//        deck.addLast(3);
//        StdOut.println(deck.first);
//        deck.addFirst(6);
//        StdOut.println(deck.first.item);
//        deck.addFirst(9);
//        StdOut.println(deck.first.item);

    }

}