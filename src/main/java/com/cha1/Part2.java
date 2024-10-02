package com.cha1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class Part2 {
    public static void main( String[] args ) {
            InputStream cwd = Part2.class.getClassLoader().getResourceAsStream("cha1/input.txt");
            if(cwd == null) return;
            Integer total = 0;

        Map<String, Integer> nameNumbersMap = Map.of(
                "zero", 0,
                "one", 1,
                "two", 2,
                "three",3,
                "four",4,
                "five",5,
                "six",6,
                "seven",7,
                "eight",8,
                "nine",9
            );

            try (BufferedReader br = new BufferedReader(new InputStreamReader(cwd))) {
                String line;

                while ((line = br.readLine()) != null) {
                    int firstIndexOfNameNumber = Integer.MAX_VALUE;
                    int firstIndexOfNameNumberValue = 0;
                    int lastIndexOfNameNumber = -1;
                    int lastNameNumberValue = 0;

                    for(String key: nameNumbersMap.keySet()) {
                        int firstIndex = line.indexOf(key);
                        int index = line.lastIndexOf(key);

                        if(firstIndex != -1 && firstIndex < firstIndexOfNameNumber) {
                            firstIndexOfNameNumber = firstIndex;
                            firstIndexOfNameNumberValue = nameNumbersMap.get(key);
                        }

                        if(index != -1 && index > lastIndexOfNameNumber) {
                            lastIndexOfNameNumber = index;
                            lastNameNumberValue = nameNumbersMap.get(key);
                        }
                    }

                    int firstDigit = firstIndexOfNameNumberValue;
                    int lastDigit = lastNameNumberValue;

                    int idx = 0;

                    for(char c:line.toCharArray()) {
                         if(Character.isDigit(c) && idx <= firstIndexOfNameNumber) {
                             firstDigit = Integer.parseInt(c + "");
                             firstIndexOfNameNumber = Integer.MIN_VALUE;
                         }

                         if(Character.isDigit(c) && idx >= lastIndexOfNameNumber) {
                             lastDigit = Integer.parseInt(c + "");
                         }

                         idx++;
                    }

                    int digit = Integer.parseInt(String.valueOf(firstDigit) + String.valueOf(lastDigit));
                    total += digit;
                }
                System.out.println(total);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
