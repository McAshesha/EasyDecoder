package ru.ashesha;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

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
        Object o = decodePacket(array.get(1), 0, lfm);
        array.remove(1);
        array.put(1, o);
    }

    public void encodeArray(JSONArray array) {
        Object o = encodePacket(array.get(1), lfm);
        array.remove(1);
        array.put(1, o);
    }

    private Object decodePacket(Object object, int addVar, int lfm) {
        try {
            if (object instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) object;
                for (int i = 0; i < jsonArray.length(); ++i) {
                    if (!(jsonArray.get(i) instanceof JSONObject) && !(jsonArray.get(i) instanceof JSONArray))
                        jsonArray.put(i, decode(jsonArray.get(i), addVar));
                    else decodePacket(jsonArray.get(i), addVar, lfm);
                }

                if (jsonArray.length() >= 5) {
                    Object firstElement = jsonArray.get(0);
                    Object secondElement = jsonArray.get(1);

                    jsonArray.put(0, jsonArray.get(jsonArray.length() - 1));
                    jsonArray.put(jsonArray.length() - 1, firstElement);
                    jsonArray.put(1, jsonArray.get(3));
                    jsonArray.put(3, secondElement);
                }

            } else if (object instanceof JSONObject) {

                JSONObject jsonObject = (JSONObject) object;
                if (addVar == 0)
                    addVar = jsonObject.has("lfm1") ? lfm : jsonObject.getInt("lfm5");


                for (String key : new HashSet<>(jsonObject.keySet())) {
                    if (jsonObject.get(key) instanceof JSONObject || jsonObject.get(key) instanceof JSONArray && !jsonObject.getJSONArray(key).isEmpty() && jsonObject.getJSONArray(key).get(0) instanceof JSONObject || jsonObject.get(key) instanceof JSONArray && !jsonObject.getJSONArray(key).isEmpty() && jsonObject.getJSONArray(key).get(0) instanceof JSONArray) {
                        jsonObject.put((String) decode(key, addVar), decodePacket(jsonObject.get(key), addVar, lfm));
                    } else if (!key.equals("lfm1") && !key.equals("lfm2") && !key.equals("lfm3") && !key.equals("lfm4") && !key.equals("lfm5")) {
                        String decodedKey = (String) decode(key, addVar);
                        if (decodedKey.equals("logo"))
                            jsonObject.put(decodedKey, jsonObject.get(key));
                        else jsonObject.put(decodedKey, decode(jsonObject.get(key), addVar));
                    }

                    jsonObject.remove(key);
                }

            }

        } catch (Throwable ignored) {
        }
        return object;
    }

    private Object decode(Object object, int lfm) {
        if (object instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) object;
            JSONArray result = new JSONArray();

            for (int i = 0; i < jsonArray.length(); ++i)
                result.put(decode(jsonArray.get(i), lfm));

            if (result.length() >= 5) {
                Object var7 = result.get(0);
                Object var4 = result.get(1);
                result.put(0, result.get(result.length() - 1));
                result.put(result.length() - 1, var7);
                result.put(1, result.get(3));
                result.put(3, var4);
            }

            return result;
        }

        if (object instanceof Boolean)
            return object;

        if (!(object instanceof String))
            object = object.toString();

        char[] charArray = ((String)object).toCharArray();

        for (int i = 0; i < charArray.length; ++i) {
            if (i == charArray.length - 1 && charArray[i] == ' ') {
                String str = new String(charArray).substring(0, i);
                try {
                    return Integer.valueOf(str);
                } catch (Throwable e) {
                    try {
                        return Double.valueOf(str);
                    } catch (Throwable ee) {
                        return str;
                    }
                }
            }
            charArray[i] = (char)(charArray[i] - lfm);
        }

        return new String(charArray);
    }

    private Object encodePacket(Object object, int lfm) {
        if (!(object instanceof JSONObject))
            return object;

        JSONObject jsonObject = ((JSONObject)object);
        if (lfm == 0)
            lfm = 100 + random.nextInt(101);

        for (String key : new HashSet<>(jsonObject.keySet())) {
            if (jsonObject.get(key) instanceof JSONObject)
                jsonObject.put((String) encode(key, lfm), encodePacket(jsonObject.get(key), lfm));
            else jsonObject.put((String) encode(key, lfm), encode(jsonObject.get(key), lfm));

            jsonObject.remove(key);
        }

        jsonObject.put("lfm5", lfm);
        return jsonObject;
    }

    private Object encode(Object object, int lfm) {
        if (!(object instanceof String) && !(object instanceof Number)) {
            if (!(object instanceof JSONArray))
                return object;

            JSONArray jsonArray = (JSONArray) object;
            JSONArray result = new JSONArray();

            for (int i = 0; i < jsonArray.length(); ++i)
                result.put(encode(jsonArray.get(i), lfm));

            if (result.length() >= 5) {
                Object firstElement = result.get(0);
                Object secondElement = result.get(1);
                result.put(0, result.get(result.length() - 1));
                result.put(result.length() - 1, firstElement);
                result.put(1, result.get(3));
                result.put(3, secondElement);
            }

            return result;
        }
        boolean isNumber = false;
        if (object instanceof Number) {
            object = object.toString();
            isNumber = true;
        }

        char[] charArray = ((String)object).toCharArray();

        for (int i = 0; i < charArray.length; ++i)
            charArray[i] = (char)(charArray[i] + lfm);

        return isNumber ? new String(charArray) + " " : new String(charArray);
    }

}
