package com.example.user.banhangonline.caches;

import android.content.Context;

import com.example.user.banhangonline.model.DonHang;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SaveMyPurchased {
    private static final String fileNameMyPurchased = "MyPurchased.txt";
    public static List<DonHang> objectPurchasedToSave = new ArrayList<>();

    //start [DonHang]
    public static void saveMyPurchasedFile(Context context, List<DonHang> objectToSave) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileNameMyPurchased, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(objectToSave);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<DonHang> readMyPurchasedFile(Context context) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileNameMyPurchased);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            objectPurchasedToSave = (List<DonHang>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objectPurchasedToSave;
    }
    //end [DonHang]
}
