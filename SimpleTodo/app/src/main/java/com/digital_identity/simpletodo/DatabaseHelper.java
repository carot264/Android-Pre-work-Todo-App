package com.digital_identity.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todo.db";
    private static final String TABLE_NAME = "todolist";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "taskName";
    private static final String COLUMN_IS_DONE = "isDone";


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME  + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_IS_DONE + " INTEGER " +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF  EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    //Add a new row to the database
    public void addTask(Task t){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  values = new ContentValues();
        values.put(COLUMN_NAME, t.getName());
        values.put(COLUMN_IS_DONE, t.isFinish());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    //delete row
    public void deleteTask(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = COLUMN_NAME + " ='" + name + "'";
        db.delete(TABLE_NAME,where,null);
        db.close();
    }
    //update row
    public void updateTask(String oldValue, Task t){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  values = new ContentValues();
        values.put("isDone",t.isFinish());
        values.put("taskName", t.getName());
        String where =  COLUMN_NAME + " ='" + oldValue + "'";
        db.update(TABLE_NAME, values, where, null);
        db.close();
    }

    public ArrayList<Task> getTaskList(){
        ArrayList<Task> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_NAME;
            //Cursor  point to a location  in  results
            Cursor c = db.rawQuery(query, null);
            //Move to the first row results
            c.moveToFirst();

            while (!c.isAfterLast()) {
                if (c.getString(c.getColumnIndex("taskName")) != null) {
                    Task t = new Task(c.getString(c.getColumnIndex("taskName")), c.getInt(c.getColumnIndex("isDone")));
                    list.add(t);
                    c.moveToNext();
                }
            }
            c.close();
            db.close();
        }catch (Exception e){
        }
        return list;
    }
}
