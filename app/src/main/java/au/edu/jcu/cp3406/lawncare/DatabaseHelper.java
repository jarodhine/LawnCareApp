package au.edu.jcu.cp3406.lawncare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LawnCare.db";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Table creation sql example
    //Able to be converted straight to MySQL, foreign key selection may require translation
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

    //Create new user example
    //To be handled server side in pure sql after live server conversion
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

    //Return true if username already exist in database
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

    //Return true if username and password pair exist in database
    //No security implemented, passwords are stored in plain text
    //Security system will need to be implemented after live server conversion
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

    //Return database ID matching username
    //ID used instead of username to prepare for live server conversion
    //ID contains no personal information and is faster to process as an integer
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

    //Add a pair of delivery and pickup task to the database
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

    //Checks day has time slots available
    //Returns false if day is fully booked
    public boolean checkDay(String day, String type) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Day FROM Deliveries WHERE Day ='" + day + "' AND Type = '" + type + "'", null);

        if (cursor.getCount() >= 8) {
            return false;
        }
        cursor.close();

        return true;
    }

    //Check time is available
    //Return false if time already booked
    public boolean checkTime(String day, String time, String type) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Day FROM Deliveries WHERE Day ='" + day + "' AND Time = '" + time + "' AND Type = '" + type + "'", null);

        if (cursor.getCount() >= 1) {
            return false;
        }
        cursor.close();

        return true;
    }

    //Checks is user has no prior delivery or pickup
    //Returns false is delivery or pickup is already scheduled
    public boolean checkExisting(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Day FROM Deliveries WHERE ID ='" + id + "'", null);

        if (cursor.getCount() >= 1) {
            return false;
        }
        cursor.close();

        return true;
    }

    //Returns string of delivery date for user
    public String getDelivery(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Day, Time FROM Deliveries WHERE ID='" + id + "' AND Type = 'Delivery'", null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return "No Delivery Scheduled";

        }

        cursor.moveToFirst();
        String day = cursor.getString(0);
        String time = cursor.getString(1);
        cursor.close();

        return day + " " + time;
    }

    //Returns string of pickup date for user
    public String getPickup(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Day, Time FROM Deliveries WHERE ID='" + id + "' AND Type = 'Pickup'", null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return "No Pickup Scheduled";

        }

        cursor.moveToFirst();
        String day = cursor.getString(0);
        String time = cursor.getString(1);
        cursor.close();

        return day + " " + time;
    }

    //Retrieves user address matching ID
    private String getAddress(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Address FROM Users WHERE ID='" + id + "'", null);
        cursor.moveToFirst();
        String address = cursor.getString(0);
        cursor.close();
        return address;
    }

    //Returns array of deliveries and pickups to be made for current day
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

    //Delete selected delivery or pickup from database
    public void removeDelivery(String time, String currentDay) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "Day=? AND Time=?";
        String[] whereArgs = new String[] {currentDay, time};
        db.delete("Deliveries", whereClause, whereArgs);
    }

    //Returns count of deliveries and pickups for current day
    public int getDeliveryCount(String currentDay) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM Deliveries WHERE Day='" + currentDay + "' ORDER BY Time", null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
