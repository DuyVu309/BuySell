package com.example.user.banhangonline.caches;

import android.content.Context;

import com.example.user.banhangonline.model.DonHang;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class SaveMyCart {
    private static final String fileNameMyCart = "MyCart.txt";

    //start [DonHang]
    public static void saveCategoryFile(Context context, List<DonHang> objectToSave) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileNameMyCart, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(objectToSave);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<DonHang> readCategoryFile(Context context, List<DonHang> objectToSave) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileNameMyCart);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            objectToSave = (List<DonHang>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objectToSave;
    }
    //end [DonHang]

}
