package au.edu.jcu.cp3406.lawncare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LawnCare.db";
    private static final String TABLE_NAME = "Users";
    private static final String Col1 = "ID";
    private static final String Col2 = "Username";
    private static final String Col3 = "Password";
    private static final String Col4 = "Name";
    private static final String Col5 = "Address";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addUser(String username, String password, String name, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username", username);
        contentValues.put("Password", password);
        contentValues.put("Name", name);
        contentValues.put("Address", address);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public boolean checkUserExist(String username) {
        String[] columns = {Col1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = Col2 + "=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int counter = cursor.getCount();
        cursor.close();
        db.close();

        return counter > 0;
    }

    public boolean checkUserPassword(String username, String password) {
        String[] columns = {Col1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = Col2 + "=?" + " and " + Col3 + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int counter = cursor.getCount();
        cursor.close();
        db.close();

        return counter > 0;
    }
}
