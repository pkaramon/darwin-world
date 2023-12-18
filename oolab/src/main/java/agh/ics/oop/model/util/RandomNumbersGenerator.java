package agh.ics.oop.model.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class RandomNumbersGenerator {
    public static List<Integer> generate(int amount, int lowerBound, int upperBound) {

        List<Integer> list = IntStream.range(lowerBound, upperBound).boxed().toList();

        Collections.shuffle(list);

        return list.subList(0, amount);
    }

}
