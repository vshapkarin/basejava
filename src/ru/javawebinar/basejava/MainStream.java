package ru.javawebinar.basejava;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainStream {
    public static void main(String[] args) {
        int[] arr = {9, 3, 2, 3, 1, 1, 4, 7};
        System.out.println(minValue(arr));

        System.out.println(oddOrEven(Arrays.asList(5, 8, 11, 12, 1, 3)));
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (acc, x) -> acc * 10 + x);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        AtomicInteger result = new AtomicInteger();
        return integers.stream()
                .peek(result::addAndGet)
                .collect(Collectors.partitioningBy(x -> x % 2 != 0))
                .get(result.get() % 2 == 0);
    }
}
