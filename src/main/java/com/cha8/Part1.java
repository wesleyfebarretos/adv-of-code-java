package com.cha8;

import com.utils.Utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Part1 {
    public static final String INITIAL_NODE = "AAA";
    public static final String FINAL_NODE = "ZZZ";

    public static void main(String[] args) throws Exception {
        List<String> file = Utils.readLine("cha8/input.txt");

        String instructions = file.getFirst();

        Map<String, String[]> nodeMap = file.stream()
            .skip(2)
            .map(toNodeArray())
            .collect(toNodeLinkedHashMap());

        System.out.println(getSteps(instructions, nodeMap));
    }

    public static Function<String, String[]> toNodeArray() {
        return (String line) -> {
            String hydratedLine = line.replaceAll("[^A-Za-z]", "");
            String nodeSource = hydratedLine.substring(0, 3);
            String node1 = hydratedLine.substring(3, 6);
            String node2 = hydratedLine.substring(6, 9);
            return new String[]{nodeSource, node1, node2};
        };
    }

    public static Collector<String[], ?, Map<String, String[]>> toNodeLinkedHashMap() {
        return Collectors.toMap(
                nodes -> nodes[0],
                nodes -> new String[]{nodes[1], nodes[2]},
                (existing, replacement) -> existing,
                LinkedHashMap::new
        );
    }

    public static int getSteps(String instructions, Map<String, String[]> nodeMap) {
        int steps = 0;
        String nodeSource = INITIAL_NODE;

        while(true) {
            for(int i = 0; i < instructions.length(); i++) {
                if(nodeSource.equals(FINAL_NODE)) return steps;
                ++steps;
                char instruction = instructions.charAt(i);
                nodeSource = nodeMap.get(nodeSource)[getNodeIndex(instruction)];
            }
        }
    }

    public static int getNodeIndex(char instruction) {
        if(instruction == 'R') return 1;

        return 0;
    }
}
