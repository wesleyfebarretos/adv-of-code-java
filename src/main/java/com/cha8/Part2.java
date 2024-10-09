package com.cha8;

import com.utils.Utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Part2 {
    public static void main(String[] args) throws Exception {
        List<String> file = Utils.readLine("cha8/input.txt");

        String instructions = file.getFirst();

        Map<String, String[]> nodeMap = file.stream()
            .skip(2)
            .map(toNodeArray())
            .collect(toNodeLinkedHashMap());

        Long steps = findStartingNodes(nodeMap).stream()
            .map(toSteps(instructions, nodeMap))
            .map(Long::valueOf)
            .reduce(1L, calcLCM());

        System.out.println(steps);
    }

    public static Function<String, Integer> toSteps(String instructions, Map<String, String[]> nodeMap) {
        return (String node) -> getSteps(instructions, nodeMap, node);
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

    public static int getSteps(String instructions, Map<String, String[]> nodeMap, String startingNode) {
        int steps = 0;

        while(true) {
            for(int i = 0; i < instructions.length(); i++) {
                if(isAnEndingNode(startingNode)) return steps;
                ++steps;
                char instruction = instructions.charAt(i);
                startingNode = nodeMap.get(startingNode)[getNodeIndex(instruction)];
            }
        }
    }

    public static List<String> findStartingNodes(Map<String, String[]> nodes) {
        return nodes.keySet()
            .stream()
            .filter(node -> node.charAt(2) == 'A')
            .toList();
    }

    public static boolean isAnEndingNode(String node) {
        return node.endsWith("Z");
    }

    public static int getNodeIndex(char instruction) {
        if(instruction == 'R') return 1;

        return 0;
    }

    public static Long calcGCD(Long a, Long b) {
        if(b == 0) return a;

        return calcGCD(b, a%b);
    }

    public static BinaryOperator<Long> calcLCM() {
        return (a, b) -> (a * b) / calcGCD(a, b);
    }
}
