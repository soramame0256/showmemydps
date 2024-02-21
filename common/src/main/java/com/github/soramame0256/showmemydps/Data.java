package com.github.soramame0256.showmemydps;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Data {
    private final String filePath;
    private final JsonObject jsonObject;
    public Data() throws IOException {
        Path directory = Paths.get("showmemydps");
        if(Files.notExists(directory)) {
            Files.createDirectory(directory);
        }
        this.filePath = "showmemydps/data.json";
        if(Files.notExists(Paths.get(filePath))){
            FileWriter fw = new FileWriter(filePath);
            PrintWriter writer = new PrintWriter(fw);
            writer.print("{}");
            writer.flush();
            writer.close();
        }
        JsonObject j;
        try {
            j = JsonParser.parseReader(new FileReader(this.filePath)).getAsJsonObject();
        }catch(IllegalStateException e){ //idk why data.json going empty. but it sometimes occurs, so.
            j = new JsonObject();
            System.out.println("datafile is broken. so using new object.");
        }
        jsonObject = j;
    }
    public void set(String key, boolean b){
        jsonObject.addProperty(key,b);
    }
    public void set(String key, int i){
        jsonObject.addProperty(key,i);
    }
    public void set(String key, String s){
        jsonObject.addProperty(key,s);
    }
    public boolean getBoolean(String key,boolean defaultValue){
        return jsonObject.has(key) ? jsonObject.get(key).getAsBoolean() : defaultValue;
    }
    public int getInteger(String key,int defaultValue){
        return jsonObject.has(key) ? jsonObject.get(key).getAsInt() : defaultValue;
    }
    public String getString(String key,String defaultValue){
        return jsonObject.has(key) ? jsonObject.get(key).getAsString() : defaultValue;
    }
    public void flush() throws IOException {
        String print = new GsonBuilder().serializeNulls().setPrettyPrinting().create()
                .toJson(jsonObject);
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(print);
        fileWriter.flush();
        fileWriter.close();

    }
}
