import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>
{
    private int N;   // size of the deque
    private Node s;  // sentinel node

    // private class for nodes in doubly-linked-list
    private class Node
    {
        private Item item;
        private Node next;
        private Node prev;

        // inner class constructor
        Node(Item i, Node n, Node p)
        {
            item = i;
            next = n;
            prev = p;
        }
    }

    // construct an empty deque
    public Deque()
    {
        N = 0;
        s = new Node(null, null, null);
        s.next = s;
        s.prev = s;
    }

    // if new item is null, do not allow
    private void validateItem(Item item)
    {
        if (item == null) {
            throw new NullPointerException("Cannot add null item");
        }
    }

    // is the deque empty?
    public boolean isEmpty()
    {
        return s.next == s && s.prev == s;
    }

    // return the number of items on the deque
    public int size()
    {
        return N;
    }

    // insert the item at the front
    public void addFirst(Item item)
    {
        // validate item is not null
        validateItem(item);

        // new first node
        Node newFirst = new Node(item, s.next, s);

        // update sentinel and old first pointers
        s.next = newFirst;
        newFirst.next.prev = newFirst;

        // increment size of deque
        N++;
    }

    // insert the item at the end
    public void addLast(Item item)
    {
        // validate item is not null
        validateItem(item);

        // new last node
        Node newLast = new Node(item, s, s.prev);

        // update sentinel and old first pointers
        s.prev = newLast;
        newLast.prev.next = newLast;

        // increment size of deque
        N++;
    }

    // delete and return the item at the front
    public Item removeFirst()
    {
        // if empty deque
        if (isEmpty()) throw new NoSuchElementException();

        // capture the current first item
        Item item = s.next.item;

        // save the 2nd node
        Node second = s.next.next;

        // avoid loitering and remove current first
        s.next = null;

        // make second node the new first one
        s.next = second;
        second.prev = s;

        // decrement the size of the deque
        N--;

        return item;
    }

    // delete and return the item at the end
    public Item removeLast() 
    {
        // if empty deque
        if (isEmpty()) throw new NoSuchElementException();

        // capture the current last item
        Item item = s.prev.item;

        // save the second last node
        Node secondLast = s.prev.prev;

        // avoid loitering and remove current last
        s.prev = null;

        // make second last node the new last one
        s.prev = secondLast;
        secondLast.next = s;

        // decrement the size of the deque
        N--;

        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() { return new ListIterator(); }

    private class ListIterator implements Iterator<Item>
    {
        // s.next is the current first node
        private Node current = s.next;

        // there is a next if this node isn't null
        public boolean hasNext() { return current != s; }

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
/*
        Deque<Integer> deck = new Deque<Integer>();

        deck.addFirst(1);
        deck.addFirst(7);
        deck.addFirst(8);
        deck.addLast(17);
        deck.addFirst(9);

        for (int i : deck) { for (int j : deck) { StdOut.println(i+" "+j); } }
*/
    }

}