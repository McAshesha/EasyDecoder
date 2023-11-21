package ru.ashesha;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

    }


    static String readFile(String name) {
        try {
            //noinspection ResultOfMethodCallIgnored
            new File(name).createNewFile();
        } catch (Throwable ignored) {
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(name)), StandardCharsets.UTF_8))) {
            String result = reader.readLine();
            return result == null ? "" : result;
        } catch (Throwable e) {
            return "";
        }
    }
}