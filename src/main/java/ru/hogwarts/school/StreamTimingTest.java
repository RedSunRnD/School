package ru.hogwarts.school;

import java.util.stream.Stream;

public class StreamTimingTest {

    public Long calculateSumSequential() {
        return Stream.iterate(1L, a -> a + 1)
                .limit(1_000_000)
                .reduce(0L, Long::sum);
    }

    public Long calculateSumParallel() {
        return Stream.iterate(1L, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0L, Long::sum);
    }

    public static void main(String[] args) {
        StreamTimingTest test = new StreamTimingTest();

        long start = System.nanoTime();
        Long result1 = test.calculateSumSequential();
        long end = System.nanoTime();
        System.out.println("Sequential result: " + result1);
        System.out.println("Sequential time: " + (end - start) / 1_000_000 + " ms");

        start = System.nanoTime();
        Long result2 = test.calculateSumParallel();
        end = System.nanoTime();
        System.out.println("Parallel result: " + result2);
        System.out.println("Parallel time: " + (end - start) / 1_000_000 + " ms");
    }
}
