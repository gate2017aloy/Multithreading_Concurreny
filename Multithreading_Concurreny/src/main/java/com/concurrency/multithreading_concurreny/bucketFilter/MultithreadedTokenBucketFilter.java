package com.concurrency.multithreading_concurreny.bucketFilter;

import java.util.*;

public class MultithreadedTokenBucketFilter {

    private long possibleTokens = 0;
    private final int MAX_TOKENS;
    private final int ONE_SECOND = 1000;

    public MultithreadedTokenBucketFilter(int maxTokens) {

        MAX_TOKENS = maxTokens;

        // Never start a thread in a constructor
        /*Daemon thread is a low priority thread that runs in background to perform tasks such as garbage collection.

        Properties:

        They can not prevent the JVM from exiting when all the user threads finish their execution.
        JVM terminates itself when all user threads finish their execution
        If JVM finds running daemon thread, it terminates the thread and after that shutdown itself. JVM does not care whether Daemon thread is running or not.
                It is an utmost low priority thread.*/
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

            // We wait or sleep for this time interval for the daemon thread to generate a token
            try {
                Thread.sleep(ONE_SECOND);
            } catch (InterruptedException ie) {
                // swallow exception
            }
        }
    }

    void getToken() throws InterruptedException {

        synchronized (this) {
            while (possibleTokens == 0) {
                this.wait();
            }
            possibleTokens--;
        }

        System.out.println(
                "Granting " + Thread.currentThread().getName() + " token at " + System.currentTimeMillis() / 1000);
    }

    public static void runTest( String args[] ) throws InterruptedException {
        Set<Thread> allThreads = new HashSet<Thread>();
        final MultithreadedTokenBucketFilter tokenBucketFilter = new MultithreadedTokenBucketFilter(1);

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
