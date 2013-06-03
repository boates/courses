public class timerExercise {
    public static void main(String[] args)
    {
        int n = 2097152;
        int seed = 804118;
        
        Stopwatch s1 = new Stopwatch();
        Timing.trial(n,seed);
        double e1 = s1.elapsedTime();
        StdOut.println(e1);
        
        Stopwatch s2 = new Stopwatch();
        Timing.trial(n,seed);
        double e2 = s2.elapsedTime();
        StdOut.println(e2);
        
        Stopwatch s3 = new Stopwatch();
        Timing.trial(n,seed);
        double e3 = s3.elapsedTime();
        StdOut.println(e3);
/*
        Stopwatch s4 = new Stopwatch();
        Timing.trial(n,seed);
        double e4 = s4.elapsedTime();
        StdOut.println(e4);
        
        Stopwatch s5 = new Stopwatch();
        Timing.trial(n,seed);
        double e5 = s5.elapsedTime();
        StdOut.println(e5);        
*/

//        StdOut.println((e1+e2+e3+e4+e5)/5.0);
        StdOut.println((e1+e2+e3)/3.0);
    }
}