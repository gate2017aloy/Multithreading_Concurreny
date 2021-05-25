package com.concurrency.multithreading_concurreny.bucketFilter;

import java.util.*;

public class TokenBucketFilter {

    private int MAX_TOKENS;
    private long lastRequestTime = System.currentTimeMillis();
    long possibleTokens = 0;


    public TokenBucketFilter(int maxTokens) {
        MAX_TOKENS = maxTokens;
    }

//    Note how getToken() doesn't return any token type !
//    The fact a thread can return from the getToken call would imply that the thread has the token,
//    which is nothing more than a permission to undertake some action.

//    Note we are using synchronized on our getToken method, this means that only a single thread can try to get a token,
//    which makes sense since we'll be computing the available tokens in a critical section.

    synchronized void getToken() throws InterruptedException {

        possibleTokens += (System.currentTimeMillis() - lastRequestTime) / 1000;

        if (possibleTokens > MAX_TOKENS) {
            possibleTokens = MAX_TOKENS;
        }

        if (possibleTokens == 0) {
            // Already the tokens are 0,
            // so current thread waits for the allotted time for a token to be produced which is indicated by the sleep
            // and gets the token and possible tokens still remain 0
            Thread.sleep(1000);
            System.out.println("Sleeping " + Thread.currentThread().getName() + " at " + (System.currentTimeMillis() / 1000));
        } else {
            possibleTokens--;
            System.out.println("deducting " + Thread.currentThread().getName() + " at " + (System.currentTimeMillis() / 1000));
        }
        lastRequestTime = System.currentTimeMillis();

        System.out.println("Granting " + Thread.currentThread().getName() + " token at " + (System.currentTimeMillis() / 1000));
    }

    public static void runTestMaxTokenIs1() throws InterruptedException {

        Set<Thread> allThreads = new HashSet<Thread>();
        final TokenBucketFilter tokenBucketFilter = new TokenBucketFilter(1);

        // Sleep for 10 seconds.
        Thread.sleep(10000);

        for (int i = 0; i < 10; i++) {

            Thread thread = new Thread(() -> {
                    try {
                        tokenBucketFilter.getToken();
                    } catch (InterruptedException ie) {
                        System.out.println("We have a problem");
                    }
            });
            thread.setName("Thread_" + (i + 1));
            allThreads.add(thread);
        }

        for (Thread t : allThreads) {
            t.start();
        }

        for (Thread t : allThreads) {
            t.join();
        }
    }

}
