package com.samiapps.kv.roobaruduniya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KV on 2/7/17.
 */

public class FavDb extends SQLiteOpenHelper {
    public static final int Database_version = 7;
    public static final String Database_name = "favdb";

    FavDb(Context context) {
        super(context, Database_name, null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try

        {
            final String SQL_CREATE_Fav_TABLE = "CREATE TABLE " + RoobaruContract.tableName + "(" + RoobaruContract.COLUMN_KEY + " TEXT NOT NULL," + RoobaruContract.category + " TEXT NOT NULL);";
           // Log.d("checksql", SQL_CREATE_Fav_TABLE);
            db.execSQL(SQL_CREATE_Fav_TABLE);

        } catch (Exception e) {
           // Log.e("ERROR", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RoobaruContract.tableName);
        onCreate(db);

    }

    public void insertKey(SQLiteDatabase db, String key, String category) {
        ContentValues cv = new ContentValues();
        cv.put(RoobaruContract.COLUMN_KEY, key);
        cv.put(RoobaruContract.category, category);
        db.insert(RoobaruContract.tableName, null, cv);

        //Log.d("insertkey", "key inserted");
        Cursor c = db.query(RoobaruContract.tableName, null, null, null, null, null, null);
        String[] columnNames = c.getColumnNames();
        Cursor res = db.rawQuery("select * from fav", null);
        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                String name = res.getString(res.getColumnIndex(RoobaruContract.category));
               // Log.d("ckvalname", name);


                res.moveToNext();
            }


        }





    }

    public void deleteKey(SQLiteDatabase db, String key, String category) {
        ContentValues cv = new ContentValues();
        cv.put(RoobaruContract.COLUMN_KEY, key);
        cv.put(RoobaruContract.category, category);

        db.delete(RoobaruContract.tableName, "key=? and category=?", new String[]{key, category});
        //Log.d("deletedkey", "key");

    }

    public Cursor getKey(SQLiteDatabase db) {
        // String[] projection={RoobaruContract.COLUMN_KEY,RoobaruContract.category};
        String s[] = {"booked"};

        Cursor c = db.rawQuery("select * from fav where category=?", s);
        //  Cursor c=db.query(RoobaruContract.tableName,projection,null,null,null,null,null);


        return c;
    }

    public Cursor queryKey(SQLiteDatabase db, String key, String categoryval) {
        String[] projection = {key, categoryval};
        // Cursor c=db.query(RoobaruContract.tableName,projection,"key=?",new String[]{key},null,null,null);
        Cursor c = db.rawQuery("select * from fav where key=? and category=?", projection);

        return c;
    }

}
