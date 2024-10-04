package com.cha7;

import com.utils.Utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Part1 {
    public static final int FIVE_OF_A_KIND = 7;
    public static final int FOUR_OF_A_KIND = 6;
    public static final int FULL_HOUSE = 5;
    public static final int THREE_OF_A_KIND = 4;
    public static final int TWO_PAIR = 3;
    public static final int ONE_PAIR = 2;
    public static final int HIGH_CARD = 1;

    public static void main(String[] args) throws Exception {
        List<String> file = Utils.readLine("cha7/input.txt");

        Map<String, Integer> handsBidMap = new HashMap<>();

        AtomicInteger index = new AtomicInteger();

        int totalWinnings = file.stream()
            .map(line -> {
                String[] handBid = line.split(" ");
                String hand = handBid[0];
                String bid = handBid[1];
                handsBidMap.put(hand, Integer.parseInt(bid));
                return hand;
            })
            .sorted(sortHand())
            .reduce(0, (accum, hand) -> accum + handsBidMap.get(hand) * index.incrementAndGet(), Integer::sum);

        System.out.println(totalWinnings);
    }

    public static Comparator<String> sortHand() {
        return (String hand1, String hand2) -> {
            int hand1Strength = getHandStrength(hand1);
            int hand2Strength = getHandStrength(hand2);

            if (hand1Strength != hand2Strength) {
                return Integer.compare(hand1Strength, hand2Strength);
            }

            String winnerHand = findWinnerFromStrengthCollision(hand1, hand2);

            return winnerHand.equals(hand1) ? 1 : -1;
        };
    }

    public static int getHandStrength(String hand) {
        Map<Character, Integer> strengthMap = hand.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(c -> c, Collectors.summingInt(c -> 1)));

        int maxEquals = Collections.max(strengthMap.values());
        int mapSize = strengthMap.keySet().size();

        return switch (maxEquals) {
            case 4 -> FOUR_OF_A_KIND;
            case 3 -> {
                if (mapSize == 2) yield FULL_HOUSE;

                yield THREE_OF_A_KIND;
            }
            case 2 -> {
                if (mapSize == 3) yield TWO_PAIR;

                yield ONE_PAIR;
            }
            case 1 -> HIGH_CARD;
            default -> FIVE_OF_A_KIND;
        };
    }

    public static String findWinnerFromStrengthCollision(String hand1, String hand2) {
        Map<Character, Integer> priorityCardsMap =  new HashMap<>();

        priorityCardsMap.put('A', 10);
        priorityCardsMap.put('K', 9);
        priorityCardsMap.put('Q', 8);
        priorityCardsMap.put('J', 7);
        priorityCardsMap.put('T', 6);
        priorityCardsMap.put('9', 5);
        priorityCardsMap.put('8', 4);
        priorityCardsMap.put('7', 3);
        priorityCardsMap.put('6', 2);
        priorityCardsMap.put('5', 1);
        priorityCardsMap.put('4', -1);
        priorityCardsMap.put('3', -2);
        priorityCardsMap.put('2', -3);

        for(int i = 0; i < hand1.length(); i++) {
            int strengthCard1 = priorityCardsMap.get(hand1.charAt(i));
            int strengthCard2 = priorityCardsMap.get(hand2.charAt(i));

            if(strengthCard1 == strengthCard2) continue;

            if(strengthCard1 > strengthCard2) return hand1;

            return hand2;
        }

        return hand1;
    }
}

