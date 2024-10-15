package com.cha10;

import com.utils.Utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Part2 {
    public static class Pipe {
        char pipe;
        boolean north;
        boolean south;
        boolean east;
        boolean west;
        boolean partOfMainLoop;
        boolean withinTheLoop;
        int x;
        int y;

        public Pipe(int y, int x, boolean west, boolean east, boolean south, boolean north, char pipe) {
            this.y = y;
            this.x = x;
            this.west = west;
            this.east = east;
            this.south = south;
            this.north = north;
            this.pipe = pipe;
        }

        public char getPipe() {
            return pipe;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setNorth(boolean north) {
            this.north = north;
        }

        public void setSouth(boolean south) {
            this.south = south;
        }

        public void setEast(boolean east) {
            this.east = east;
        }

        public void setWest(boolean west) {
            this.west = west;
        }

        public Optional<String> findNextDirection(String incomingDirection) {
            String oppositeDirection = switch (incomingDirection) {
                case "west" -> "east";
                case "east" -> "west";
                case "north" -> "south";
                case "south" -> "north";
                default -> null;
            };

            Map<String, Boolean> directions = Map.of(
                    "north", this.north,
                    "south", this.south,
                    "east", this.east,
                    "west", this.west
            );

            return directions.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals(oppositeDirection) && entry.getValue())
                    .map(Map.Entry::getKey)
                    .findFirst();
        }

        public boolean isPartOfMainLoop() {
            return partOfMainLoop;
        }

        public void setPartOfMainLoop(boolean partOfMainLoop) {
            this.partOfMainLoop = partOfMainLoop;
        }

        public boolean isWithinTheLoop() {
            return withinTheLoop;
        }

        public void setWithinTheLoop(boolean withinTheLoop) {
            this.withinTheLoop = withinTheLoop;
        }

        @Override
        public String toString() {
            return "Pipe{" +
                    "pipe=" + pipe +
                    ", north=" + north +
                    ", south=" + south +
                    ", east=" + east +
                    ", west=" + west +
                    ", partOfMainLoop=" + partOfMainLoop +
                    ", withinTheLoop=" + withinTheLoop +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
    public static void main(String[] args) throws Exception {
        List<String> file = Utils.readLine("cha10/input.txt");

        AtomicInteger index = new AtomicInteger(0);

        List<List<Pipe>> pipes = file.stream()
            .map(line -> {
                int y = index.getAndIncrement();
                return toPipes(y, line);
            })
            .toList();

        Pipe sPipe = pipes.stream()
                .flatMap(List::stream)
                .filter(pipe -> pipe.getPipe() == 'S')
                .findFirst()
                .orElse(null);

        if(sPipe == null) {
            System.out.println("S pipe not found");
            return;
        }

        char sPipeidentity = findSPipeIdentity(sPipe.getX(), sPipe.getY(), pipes);

        overrideSPipeWithIdentity(sPipeidentity, sPipe.getX(), sPipe.getY(), pipes);

        if(sPipe.west) setPipesFromMainLoop("west", pipes, sPipe);
        if(sPipe.east) setPipesFromMainLoop("east", pipes, sPipe);
        if(sPipe.north) setPipesFromMainLoop("north", pipes, sPipe);
        if(sPipe.south) setPipesFromMainLoop("south", pipes, sPipe);

        findPipesWithinTheLoop(pipes);

        int pipesWithinTheLoop = pipes.stream()
            .flatMap(List::stream)
            .filter(Pipe::isWithinTheLoop)
            .toList()
            .size();

        System.out.println(pipesWithinTheLoop);
    }

    public static void findPipesWithinTheLoop(List<List<Pipe>> pipes) {
        List<Character> acrossPipes = new ArrayList<>(Arrays.asList('J', 'L', '|'));

        for (List<Pipe> pipeList : pipes) {
            for (int x = 0; x < pipeList.size(); x++) {
                int count = 0;

                if(pipeList.get(x).isPartOfMainLoop()) continue;

                for (int x2 = 0; x2 < x; x2++) {
                    Pipe pipe = pipeList.get(x2);

                    if(!pipe.isPartOfMainLoop()) continue;

                    if (acrossPipes.contains(pipe.getPipe() == 'S'
                        ? findSPipeIdentity(pipe.getX(), pipe.getY(), pipes)
                        : pipe.getPipe())
                    ) {
                        count++;
                    }
                }
                if ((count & 1) == 1) {
                    pipeList.get(x).setWithinTheLoop(true);
                }
            }
        }
    }

    public static List<Pipe> toPipes(int y, String line) {
        AtomicInteger index = new AtomicInteger(0);

        return line.chars()
                .mapToObj(c -> (char) c)
                .map(pipe -> {
                    int currentIndex = index.getAndIncrement();
                    return toPipe(currentIndex, y, pipe);
                })
                .toList();
    }

    public static Pipe toPipe(int x, int y, char pipe) {
        boolean west = getWestConnection(pipe);
        boolean east = getEastConnection(pipe);
        boolean south = getSouthConnection(pipe);
        boolean north = getNorthConnection(pipe);

        return new Pipe(y,x, west, east, south, north, pipe);
    }

    public static boolean getWestConnection(char pipe) {
        return pipe == '-' || pipe == 'J' || pipe == '7';
    }

    public static boolean getEastConnection(char pipe) {
        return pipe == '-' || pipe == 'L' || pipe == 'F';
    }

    public static boolean getSouthConnection(char pipe) {
        return pipe == '|' || pipe == '7' || pipe == 'F';
    }

    public static boolean getNorthConnection(char pipe) {
        return pipe == '|' || pipe == 'L' || pipe == 'J';
    }

    public static char findSPipeIdentity(int x, int y, List<List<Pipe>> pipes) {
        Pipe northPipe = pipes.get(y - 1).get(x);
        Pipe southPipe = pipes.get(y + 1).get(x);
        Pipe eastPipe = pipes.get(y).get(x + 1);
        Pipe weastPipe = pipes.get(y).get(x - 1);

        if(northPipe.south && southPipe.north) return '|';
        if(northPipe.south && weastPipe.east) return 'J';
        if(northPipe.south && eastPipe.west) return 'L';
        if(southPipe.north && eastPipe.west) return 'F';
        if(southPipe.north && weastPipe.east) return '7';

        return '-';
    }

    public static void overrideSPipeWithIdentity(char pipe, int x, int y, List<List<Pipe>> pipes) {
        boolean west = getWestConnection(pipe);
        boolean east = getEastConnection(pipe);
        boolean south = getSouthConnection(pipe);
        boolean north = getNorthConnection(pipe);

        pipes.get(y).get(x).setWest(west);
        pipes.get(y).get(x).setEast(east);
        pipes.get(y).get(x).setNorth(north);
        pipes.get(y).get(x).setSouth(south);
        pipes.get(y).get(x).setPartOfMainLoop(true);
    }

    public static void setPipesFromMainLoop(String direction, List<List<Pipe>> pipes, Pipe pipe) {
        Pipe nextPipe = pipes.get(nextY(pipe.getY(), direction)).get(nextX(pipe.getX(), direction));
        Optional<String> nextDirection  = nextPipe.findNextDirection(direction);

        while(nextDirection.isPresent()) {
            nextPipe.setPartOfMainLoop(true);

            if (nextPipe.getPipe() == 'S') return;

            nextPipe = pipes.get(nextY(nextPipe.getY(), nextDirection.get())).get(nextX(nextPipe.getX(), nextDirection.get()));

            nextDirection = nextPipe.findNextDirection(nextDirection.get());
        }
    }

    public static int nextX(int x, String direction) {
        return switch(direction) {
            case "east" -> x + 1;
            case "west" -> x - 1;
            default -> x;
        };
    }

    public static int nextY(int y, String direction) {
        return switch(direction) {
            case "north" -> y - 1;
            case "south" -> y + 1;
            default -> y;
        };
    }
}