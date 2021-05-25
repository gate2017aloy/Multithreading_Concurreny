package com.concurrency.multithreading_concurreny.bucketFilter.factoryDesignPattern;


/*

    The problem with the above solution is that we start our thread in the constructor.
    Never start a thread in a constructor as the child thread can attempt to use the not-yet-fully constructed object using this.
    This is an anti-pattern. Some candidates present this solution when attempting to solve token bucket filter problem using threads.
    However, when checked, few candidates can reason why starting threads in a constructor is a bad choice.

    There are two ways to overcome this problem,
    the naive but correct solution is to start the daemon thread outside of the MultithreadedTokenBucketFilter object.
    However, the con of this approach is that the management of the daemon thread spills outside the class.
    Ideally, we want the class to encapsulate all the operations related with the management of the token bucket filter and only expose the public API to the consumers of our class, as per good object orientated design.
    This situation is a great for using the Simple Factory design pattern.
    We'll create a factory class which produces token bucket filter objects and also starts the daemon thread only when the object is full constructed.
    If you are unaware of this pattern, I'll take the liberty insert a shameless marketing plug here and refer you to this design patterns course to get up to speed.

    Our token bucket filter factory will expose a method makeTokenBucketFilter() that will return an object of type token bucket filter. Before returning the object we'll start the daemon thread. Additionally, we don't want consumers to be able to instantiate the token bucket filter objects without interacting with the factory. For this reason, we'll make the class MultithreadedTokenBucketFilter private and nest it within the factory class. We'll also add an abstract TokenBucketFilter class that consumers can use to reference the object returned from our makeTokenBucketFilter() method. The class MultithreadedTokenBucketFilter will extend the abstract class TokenBucketFilter.
*/

//https://refactoring.guru/design-patterns/factory-comparison

public class TokenBucketFilterFactory {

    // Force users to interact with the factory
    // only through the static methods
    private TokenBucketFilterFactory() {
    }

    static public TokenBucketFilter makeTokenBucketFilter(int capacity) {
        MultithreadedTokenBucketFilter tbf = new MultithreadedTokenBucketFilter(capacity);
        tbf.initialize();
        return tbf;
    }

    private static class MultithreadedTokenBucketFilter extends TokenBucketFilter {
        private long possibleTokens = 0;
        private final int MAX_TOKENS;
        private final int ONE_SECOND = 1000;

        // MultithreadedTokenBucketFilter object can only
        MultithreadedTokenBucketFilter(int maxTokens) {
            MAX_TOKENS = maxTokens;
        }

        void initialize() {
            // Never start a thread in a constructor
            Thread dt = new Thread(() -> {
                daemonThread();
            });
            dt.setDaemon(true);
            dt.start();
        }

        private void daemonThread() {

            while (true) {
                synchronized (this) {
                    if (possibleTokens < MAX_TOKENS) {
                        possibleTokens++;
                    }
                    this.notify();
                }
                try {
                    Thread.sleep(ONE_SECOND);
                } catch (InterruptedException ie) {
                    // swallow exception
                }
            }
        }

        public void getToken() throws InterruptedException {

            synchronized (this) {
                while (possibleTokens == 0) {
                    this.wait();
                }
                possibleTokens--;
            }

            System.out.println(
                    "Granting " + Thread.currentThread().getName() + " token at " + System.currentTimeMillis() / 1000);
        }
    }

}
