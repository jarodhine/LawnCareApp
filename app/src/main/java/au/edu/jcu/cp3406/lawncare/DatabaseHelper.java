package au.edu.jcu.cp3406.lawncare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LawnCare.db";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "Username TEXT,"
                    + "Password TEXT,"
                    + "Name TEXT,"
                    + "Address TEXT);");

        db.execSQL("CREATE TABLE Deliveries (ID INTEGER, "
                + "Day TEXT,"
                + "Time TEXT,"
                + "Type TEXT,"
                + "FOREIGN KEY (ID) REFERENCES Users(ID));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "Users");
        db.execSQL("DROP TABLE IF EXISTS " + "Deliveries");
        onCreate(db);
    }

    public void addUser(String username, String password, String name, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username", username);
        contentValues.put("Password", password);
        contentValues.put("Name", name);
        contentValues.put("Address", address);
        db.insert("Users", null, contentValues);
        db.close();
    }

    public boolean checkUserExist(String username) {
        String[] columns = {"ID"};
        SQLiteDatabase db = getReadableDatabase();
        String selection = "Username" + "=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query("Users", columns, selection, selectionArgs, null, null, null);
        int counter = cursor.getCount();
        cursor.close();
        db.close();

        return counter > 0;
    }

    public boolean checkUserPassword(String username, String password) {
        String[] columns = {"ID"};
        SQLiteDatabase db = getReadableDatabase();
        String selection = "Username" + "=?" + " and " + "Password" + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query("Users", columns, selection, selectionArgs, null, null, null);
        int counter = cursor.getCount();
        cursor.close();
        db.close();

        return counter > 0;
    }

    public int getID(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM Users WHERE Username='" + username + "'", null);
        int ID = 0;
        if (cursor.moveToFirst()) {
            ID = cursor.getInt(0);
        }
        cursor.close();
        return ID;
    }

    public void addDelivery(int ID, String devTime, String devDay, String picTime, String picDay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues devValues = new ContentValues();
        ContentValues picValues = new ContentValues();

        devValues.put("ID", ID);
        devValues.put("Day", devDay);
        devValues.put("Time", devTime);
        devValues.put("Type", "Delivery");
        db.insert("Deliveries", null, devValues);

        picValues.put("ID", ID);
        picValues.put("Day", picDay);
        picValues.put("Time", picTime);
        picValues.put("Type", "Pickup");
        db.insert("Deliveries", null, picValues);

        db.close();
    }

    public boolean checkDay(String day, String type) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Day FROM Deliveries WHERE Day ='" + day + "' AND Type = '" + type + "'", null);

        if (cursor.getCount() >= 8) {
            return false;
        }
        cursor.close();

        return true;
    }

    public boolean checkTime(String day, String time, String type) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Day FROM Deliveries WHERE Day ='" + day + "' AND Time = '" + time + "' AND Type = '" + type + "'", null);

        if (cursor.getCount() >= 1) {
            return false;
        }
        cursor.close();

        return true;
    }

    public boolean checkExisting(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Day FROM Deliveries WHERE ID ='" + id + "'", null);

        if (cursor.getCount() >= 1) {
            return false;
        }
        cursor.close();

        return true;
    }

    public String getDelivery(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Day, Time FROM Deliveries WHERE ID='" + id + "' AND Type = 'Delivery'", null);

        String day = "";
        String time = "";

        if (cursor.getCount() <= 0) {
            cursor.close();
            return "No Delivery Scheduled";

        }

        cursor.moveToFirst();
        day = cursor.getString(0);
        time = cursor.getString(1);
        cursor.close();

        String s = day + " " + time;
        return s;
    }

    public String getPickup(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Day, Time FROM Deliveries WHERE ID='" + id + "' AND Type = 'Pickup'", null);

        String day = "";
        String time = "";

        if (cursor.getCount() <= 0) {
            cursor.close();
            return "No Pickup Scheduled";

        }

        cursor.moveToFirst();
        day = cursor.getString(0);
        time = cursor.getString(1);
        cursor.close();

        String s = day + " " + time;
        return s;
    }

    public String getAddress(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Address FROM Users WHERE ID='" + id + "'", null);
        cursor.moveToFirst();
        String address = cursor.getString(0);
        cursor.close();
        return address;
    }

    public ArrayList<DeliveryItem> getList(String currentDay) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Time, Type, ID FROM Deliveries WHERE Day='" + currentDay + "' ORDER BY Time", null);

        ArrayList<DeliveryItem> array = new ArrayList<>();

        if (cursor.getCount() < 1) {
            array.add(new DeliveryItem("", "No Deliveries or Pickups", ""));
            return array;
        }

        while (cursor.moveToNext()) {
            String address = getAddress(cursor.getInt(2));
            array.add(new DeliveryItem(cursor.getString(0), cursor.getString(1), address));
        }


        cursor.close();
        return array;
    }

    public void removeDelivery(String time, String currentDay) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "Day=? AND Time=?";
        String[] whereArgs = new String[] {currentDay, time};
        db.delete("Deliveries", whereClause, whereArgs);
    }

    public int getDeliveryCount(String currentDay) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM Deliveries WHERE Day='" + currentDay + "' ORDER BY Time", null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
