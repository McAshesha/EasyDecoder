package ru.ashesha;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) {
        boolean encode = false;
        String input = "input.json", output = "output.json";

        for (String arg : args) {
            if (arg.equalsIgnoreCase("-encode"))
                encode = true;

            else if (arg.equalsIgnoreCase("-decode"))
                encode = false;

            else if (arg.startsWith("-in=")) {
                checkFormatInputOutput(arg);
                input = arg.substring(4);

            } else if (arg.startsWith("-out=")) {
                checkFormatInputOutput(arg);
                output = arg.substring(5);
            }

        }


    }

    static void checkFormatInputOutput(String data) {
        if (data.endsWith(".json") || data.endsWith(".txt"))
            return;
        System.out.println("Only .txt and .json file formats are supported.");
        exit(-1);
    }


    static String readFile(String name) {
        createFile(name);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(name)), StandardCharsets.UTF_8))) {
            String result = reader.readLine();
            return result == null ? "" : toUTF8(result);
        } catch (Throwable e) {
            return "";
        }
    }

    static void write(String name, String text) {
        createFile(name);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(name))))) {
            writer.write(toUTF8(text));
            writer.flush();
        } catch (Throwable ignored) {
        }
    }

    static String toUTF8(String line) {
        int index = 0;
        char[] chars = line.toCharArray();

        StringBuilder builder = new StringBuilder();
        while (index < chars.length) {
            char symbol = chars[index ++];
            if (symbol == '\\' && index + 4 < chars.length) {
                symbol = chars[index ++];

                if (symbol == 'u') {
                    String character = line.substring(index, index + 4);
                    symbol = (char) Integer.parseInt(character, 16);
                    index += 4;
                }

            }
            builder.append(symbol);
        }
        return builder.toString();
    }

    static void createFile(String name) {
        try {
            //noinspection ResultOfMethodCallIgnored
            new File(name).createNewFile();
        } catch (Throwable ignored) {
        }
    }

}