package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileReader;

public class Util {
    public static String readFile(String filename){
        StringBuilder result = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) !=  null) {
                result.append(line);
                result.append("\n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return result.toString();
    }

    public static String getValueByKeyJSON(String json, String key) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        String value = String.valueOf(jsonObject.get(key));
        value = value.substring(1, value.length() - 1);

        return value;
    }
}
