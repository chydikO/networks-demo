package org.itstep;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class ArrayMinMaxDemo {

    public static final int THREAD = 4;
    public static final int SIZE = 100_000;

    record MinMaxValues(int min, int max) {
    }

    record FindMinMax(int start, int end, int[] array) implements Callable<MinMaxValues> {
        @Override
        public MinMaxValues call() {
            int min = array[start];
            int max = array[start];
            for (int i = start; i < end; i++) {
                min = Math.min(min, array[i]);
                max = Math.max(max, array[i]);
            }
            return new MinMaxValues(min, max);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD);
        Random random = new Random();
        int[] array = random.ints(SIZE).toArray();
        List<Callable<MinMaxValues>> tasks = new ArrayList<>();
        int part = SIZE / THREAD;
        for (int i = 0; i < THREAD; i++) {
            tasks.add(new FindMinMax(i * part, (i + 1) * part, array));
        }
        List<Future<MinMaxValues>> futures = executorService.invokeAll(tasks);
        int min = array[0];
        int max = array[0];
        for (Future<MinMaxValues> future: futures) {
            MinMaxValues minMaxValues = future.get();
            min = Math.min(min, minMaxValues.min);
            max = Math.max(max, minMaxValues.max);
        }
        System.out.println("max = " + max);
        System.out.println("min = " + min);
        executorService.shutdown();
    }
}
