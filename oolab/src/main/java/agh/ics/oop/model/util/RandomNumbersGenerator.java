package agh.ics.oop.model.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class RandomNumbersGenerator {
    public static List<Integer> generate(int amount, int lowerBound, int upperBound) {

        List<Integer> list = new ArrayList<>(IntStream.range(lowerBound, upperBound).boxed().toList());

        Collections.shuffle(list);

        return list.subList(0, amount);
    }
    public static <T> List<T> generateRandomSubset(List<T> list, int amount) {
        List<T> copy = new ArrayList<>(list);
        Collections.shuffle(copy);
        return copy.subList(0, amount);
    }

}
