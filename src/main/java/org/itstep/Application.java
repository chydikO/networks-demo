package org.itstep;

import java.util.concurrent.*;

public class Application {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newCachedThreadPool();
                // Executors.newFixedThreadPool(2);
                // Executors.newSingleThreadScheduledExecutor();

        executorService.submit(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i + " " + Thread.currentThread().getName() + " isDaemon "
                + Thread.currentThread().isDaemon());
            }
        });

        executorService.submit(() -> {
            for (int i = 10; i < 20; i++) {
                System.out.println(i + " " + Thread.currentThread().getName()  + " isDaemon "
                        + Thread.currentThread().isDaemon());
            }
        });

        Future<Long> future = executorService.submit(() -> {
            long sum = 0;
            for (int i = 0; i < 100_000; i++) {
                sum += i;
            }
            return sum;
        });

        Long result = future.get(); // get() - block method similar to join()
        System.out.println("result = " + result);

        executorService.shutdown();
    }
}
