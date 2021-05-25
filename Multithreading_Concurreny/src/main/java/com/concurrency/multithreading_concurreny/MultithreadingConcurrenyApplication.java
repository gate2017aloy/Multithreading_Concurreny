package com.concurrency.multithreading_concurreny;

import com.concurrency.multithreading_concurreny.bounded_buffer.DemonstrationOfBoundedBuffer;
import com.concurrency.multithreading_concurreny.bucketFilter.TokenBucketFilter;
import com.concurrency.multithreading_concurreny.bucketFilter.factoryDesignPattern.Demonstration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.concurrency.multithreading_concurreny.bucketFilter.factoryDesignPattern.Demonstration.demo;

@SpringBootApplication
public class MultithreadingConcurrenyApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(MultithreadingConcurrenyApplication.class, args);
        DemonstrationOfBoundedBuffer.demo();
//        TokenBucketFilter.runTestMaxTokenIs1();
//        Demonstration.demo();
    }

}
