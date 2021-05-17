package com.concurrency.multithreading_concurreny;

import com.concurrency.multithreading_concurreny.bounded_buffer.DemonstrationOfBoundedBuffer;
import com.concurrency.multithreading_concurreny.bucketFilter.TokenBucketFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultithreadingConcurrenyApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(MultithreadingConcurrenyApplication.class, args);
//        DemonstrationOfBoundedBuffer.demo();
        TokenBucketFilter.runTestMaxTokenIs1();
    }

}
