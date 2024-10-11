package com.cha9;

import com.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Part2 {
    public static void main(String[] args) throws Exception {
        List<String> file = Utils.readLine("cha9/input.txt");

        int total = file.stream()
            .map(Part2::toList)
            .map(Part2::calcExtrapolation)
            .reduce(0, Integer::sum);

        System.out.println(total);

    }

    static List<Integer> toList(String line) {
        return Stream.of(line.split(" "))
                .map(Integer::valueOf)
                .toList();
    }

    static int calcExtrapolation(List<Integer> values) {
        List<Integer> extrapolatedValues = IntStream.range(0, values.size() - 1)
            .map(i -> values.get(i + 1) - values.get(i))
            .boxed()
            .collect(Collectors.toList());

        if(extrapolatedValues.stream().allMatch(value -> value == 0)) {
            return values.getFirst();
        }

        return values.getFirst() - calcExtrapolation(extrapolatedValues);
    }
}
