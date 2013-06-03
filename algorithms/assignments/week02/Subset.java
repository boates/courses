public class Subset {

    public static void main(String[] args) {

        // initialize k and check for command line args
        int k = -1;
        if (args.length > 0) {
            try { k = Integer.parseInt(args[0]); }
            catch (NumberFormatException e) {
                System.err.println("Argument" + " must be an integer");
//                System.exit(1);
            }
        }

        // initialize randomized queue object
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        // keep checking for standard input
        while (!StdIn.isEmpty())
        {
            // get a string from standard input
            String s = StdIn.readString();

            // add the string to the randomized queue
            rq.enqueue(s);
        }

        //// now we have all of our items from standard input   ////
        //// we can now print out k of them uniformly at random ////

        // loop up to k and dequeue a random item to standard output
        for (int i = 0; i < k; i++) {
            StdOut.println(rq.dequeue());
        }

    }
}