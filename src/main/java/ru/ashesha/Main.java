package ru.ashesha;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

    }


    static String readFile(String name) {
        createFile(name);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(name)), StandardCharsets.UTF_8))) {
            String result = reader.readLine();
            return result == null ? "" : result;
        } catch (Throwable e) {
            return "";
        }
    }

    static void write(String name, String text) {
        createFile(name);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(name))))) {
            writer.write(text);
            writer.flush();
        } catch (Throwable ignored) {
        }
    }

    static void createFile(String name) {
        try {
            //noinspection ResultOfMethodCallIgnored
            new File(name).createNewFile();
        } catch (Throwable ignored) {
        }
    }

}