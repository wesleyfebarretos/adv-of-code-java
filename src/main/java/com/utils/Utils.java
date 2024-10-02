package com.utils;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Utils {
    public static List<String> readLine(String file) throws Exception {
        URI path = Utils.class.getClassLoader().getResource(file).toURI();

        return Files.readAllLines(Paths.get(path));
    }
}
