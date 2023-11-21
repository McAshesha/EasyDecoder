package ru.ashesha;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Decoder {

    private static Decoder INSTANCE = null;

    public static Decoder getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Decoder();
        return INSTANCE;
    }


    private int lfm = 0;
    private final Random random = new Random();

    private Decoder() {
    }

    public void readLfm(int lfm) {
        this.lfm = lfm;
    }

    public void decodeArray(JSONArray array) {
        Object o = decodePacket(array.get(1), 0);
        array.remove(1);
        array.put(1, o);
    }

    public void encodeArray(JSONArray array) {
        Object o = encodePacket(array.get(1));
        array.remove(1);
        array.put(1, o);
    }

    private Object decodePacket(Object input, int var1) {
        try {
            if (input == null)
                return null;

            if (input instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) input;

                for (int i = 0; i < jsonArray.length(); ++i) {
                    Object element = jsonArray.get(i);
                    if (element instanceof JSONObject || element instanceof JSONArray)
                        jsonArray.put(i, decodePacket(element, var1));

                    else jsonArray.put(i, decode(element, var1));

                }

                swapArrayElements(jsonArray);
                return jsonArray;
            }

            if (input instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) input;

                if (var1 == 0)
                    var1 = jsonObject.has("lfm1") ? lfm : jsonObject.getInt("lfm5");

                List<String> keys = new ArrayList<>(jsonObject.keySet());

                for (String key : keys) {
                    Object value = jsonObject.get(key);

                    if ((value instanceof JSONObject || value instanceof JSONArray) &&
                            ((!((JSONArray) value).isEmpty() && ((JSONArray) value).get(0) instanceof JSONObject) ||
                                    (!((JSONArray) value).isEmpty() && ((JSONArray) value).get(0) instanceof JSONArray)))
                        jsonObject.put((String) decode(key, var1), decodePacket(value, var1));

                    else if (!isLFMKey(key)) {
                        String decodedKey = (String) decode(key, var1);
                        if (decodedKey.equals("logo"))
                            jsonObject.put(decodedKey, value);

                        else jsonObject.put(decodedKey, decode(value, var1));
                    }

                    jsonObject.remove(key);
                }
            }

            return input;
        } catch (ArrayIndexOutOfBoundsException e) {
            return input;
        }
    }

    private void swapArrayElements(JSONArray jsonArray) {
        if (jsonArray.length() < 5)
            return;

        Object firstElement = jsonArray.get(0);
        Object secondElement = jsonArray.get(1);
        jsonArray.put(0, jsonArray.get(jsonArray.length() - 1));
        jsonArray.put(jsonArray.length() - 1, firstElement);
        jsonArray.put(1, jsonArray.get(3));
        jsonArray.put(3, secondElement);
    }

    private Object decode(Object input, int var1) {
        if (input instanceof JSONArray) {
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < ((JSONArray) input).length(); ++i)
                jsonArray.put(decode(((JSONArray) input).get(i), var1));

            swapArrayElements(jsonArray);
            return jsonArray;
        }

        if (input instanceof Boolean)
            return input;

        String stringValue = input instanceof String ? (String) input : input.toString();
        char[] charArray = stringValue.toCharArray();

        for (int i = 0; i < charArray.length; ++i) {
            if (i == charArray.length - 1 && charArray[i] == ' ') {
                try {
                    return Integer.valueOf(new String(charArray, 0, i));
                } catch (Exception e) {
                    try {
                        return Double.valueOf(new String(charArray, 0, i));
                    } catch (Exception ee) {
                        return new String(charArray, 0, i);
                    }
                }
            }
            charArray[i] = (char) (charArray[i] - var1);
        }

        return new String(charArray);
    }

    private Object encodePacket(Object input) {
        if (!(input instanceof JSONObject))
            return input;

        JSONObject jsonObject = (JSONObject) input;

        if (lfm == 0)
            lfm = 100 + random.nextInt(101);

        List<String> keys = new ArrayList<>(jsonObject.keySet());

        for (String key : keys) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject)
                jsonObject.put((String) encode(key, lfm), encodePacket(value));
            else jsonObject.put((String) encode(key, lfm), encode(value, lfm));

            jsonObject.remove(key);
        }

        jsonObject.put("lfm5", lfm);
        return jsonObject;
    }

    private Object encode(Object input, int var2) {
        if (!(input instanceof String) && !(input instanceof Number)) {
            if (!(input instanceof JSONArray))
                return input;
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < ((JSONArray) input).length(); ++i)
                jsonArray.put(encode(((JSONArray) input).get(i), var2));

            swapArrayElements(jsonArray);
            return jsonArray;

        } else {
            boolean isNumber = false;
            if (input instanceof Number) {
                input = input.toString();
                isNumber = true;
            }

            String stringValue = (String) input;
            char[] charArray = stringValue.toCharArray();
            for (int i = 0; i < charArray.length; ++i)
                charArray[i] = (char) (charArray[i] + var2);

            return isNumber ? new String(charArray) + " " : new String(charArray);
        }
    }

    private boolean isLFMKey(String key) {
        return key.startsWith("lfm");
    }

}
