package com.cha1;

import java.io.*;

public class Part1 {
    public static void main( String[] args ) {
            InputStream cwd = Part1.class.getClassLoader().getResourceAsStream("cha1/input.txt");
            if(cwd == null) return;
            Integer total = 0;

            try (BufferedReader br = new BufferedReader(new InputStreamReader(cwd))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String digitsInLine = line.replaceAll("\\D", "");
                    String calibration = digitsInLine.charAt(0) + "" + digitsInLine.charAt(digitsInLine.length() - 1);
                    total += Integer.parseInt(calibration);
                }

                System.out.println(total);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
