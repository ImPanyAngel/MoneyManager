package com.example.moneymanager;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String TAG = "FileUtils";

    public static <T> void saveToFile(File context, String fileName, List<T> data) {
        try {
            FileOutputStream writer = new FileOutputStream(new File(context, fileName));

            for (T object : data) {
                Gson gson = new Gson();
                String json = gson.toJson(object);
                writer.write(json.getBytes());
                writer.write(System.lineSeparator().getBytes());
            }

            writer.close();
            Log.d(TAG, "Data saved to file: " + fileName);
        } catch (Exception e) {
            Log.e(TAG, "Error saving data to file: " + fileName, e);
        }
    }

    public static <T> List<T> readFromFile(File context, String fileName, Class<T> objectClass) {
        List<T> dataList = new ArrayList<>();
        try {
            File file = new File(context, fileName);
            if (!file.exists()) {
                return dataList; // Return empty list if the file doesn't exist
            }

            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            Gson gson = new Gson();

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {  // Check if the line is not empty
                    T object = gson.fromJson(line, (Type) objectClass); // Deserialize JSON string to object of type T
                    dataList.add(object); // Add the object to the list
                }
            }

            reader.close();
            Log.d(TAG, "Data read from file: " + fileName);
        } catch (Exception e) {
            Log.e(TAG, "Error reading data from file: " + fileName, e);
        }
        return dataList;
    }
}
