package ru.ashesha;

import org.json.JSONArray;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) {
        boolean encode = false;
        String input = "input", output = "output";
        Decoder decoder = Decoder.getInstance();

        for (String arg : args) {
            if (arg.equalsIgnoreCase("-encode"))
                encode = true;

            else if (arg.equalsIgnoreCase("-decode"))
                encode = false;

            else if (arg.startsWith("-lfm=")) {
                try {
                    decoder.readLfm(Integer.parseInt(arg.substring(5)));
                } catch (Throwable ignored) {
                    System.out.println("Invalid lfm format.");
                    exit(-1);
                }

            } else if (arg.startsWith("-in="))
                input = arg.substring(4);

            else if (arg.startsWith("-out="))
                output = arg.substring(5);
        }

        try {
            JSONArray array = new JSONArray(read(input));
            if (encode)
                decoder.encodeArray(array);
            else decoder.decodeArray(array);

            write(output, array.toString());
            System.out.println("Read the packet from " + input + " and display the result in " + output + ".");
        } catch (Throwable ignored) {
            System.out.println("Invalid packet format in " + input + ".");
            exit(-1);
        }
    }

    static String read(String name) {
        if (name.equalsIgnoreCase("console")) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
                return reader.readLine();
            } catch (Throwable ignored) {
                return "";
            }
        }
        return readFile(name + ".json");
    }

    static void write(String name, String text) {
        if (name.equalsIgnoreCase("console")) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))) {
                writer.write(text);
                writer.flush();
            } catch (Throwable ignored) {
            }
        } else writeInFile(name + ".json", text);
    }



    static String readFile(String name) {
        createFile(name);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(name)), StandardCharsets.UTF_8))) {
            String result = reader.readLine();
            return result == null ? "" : toUTF8(result);
        } catch (Throwable ignored) {
            return "";
        }
    }

    static void writeInFile(String name, String text) {
        createFile(name);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(name)), StandardCharsets.UTF_8))) {
            writer.write(toUTF8(text));
            writer.flush();
        } catch (Throwable ignored) {
        }
    }

    public static String toUTF8(String line) {
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