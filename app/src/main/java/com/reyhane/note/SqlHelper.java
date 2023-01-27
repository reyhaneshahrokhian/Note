package com.reyhane.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SqlHelper extends SQLiteOpenHelper {

    Context context;
    static final String DbName = "Note.db";
    static final int DbVersion = 1;
    static final String TableName = "Notes";
    static final String ID = "ID";
    static final String Title = "Title";
    static final String Description = "Description";
    static final String Image = "Image";
    static final String isFavorite = "isFavorite";

    public SqlHelper(@Nullable Context context) {
        super(context, DbName, null, DbVersion);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TableName +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Title + " Text, " +
                Description + " Text, " +
                Image + " INTEGER, " +
                isFavorite + " INTEGER DEFAULT 0);";

        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop TABLE IF EXISTS " + TableName);
        onCreate(db);
    }
    //getting data from database
    public Cursor GetData(){
        String query = "SELECT * FROM " + TableName;
        SQLiteDatabase sql = this.getReadableDatabase();

        Cursor tables = null;
        if (sql != null) {
            tables = sql.rawQuery(query,null);
        }
        return tables;
    }
    //adding data to database
    public void AddNotes(String title, String desc, int image){
        ContentValues contents = new ContentValues();
        contents.put(Title, title);
        contents.put(Description, desc);
        contents.put(Image, image);

        SQLiteDatabase sql = this.getWritableDatabase();
        sql.insert(TableName, null, contents);
    }
    //editing some data
    public void EditData(String title, String desc, int image, String id){
        ContentValues contents = new ContentValues();
        contents.put(Title, title);
        contents.put(Description, desc);
        contents.put(Image, image);

        SQLiteDatabase sql = this.getWritableDatabase();
        sql.update(TableName, contents, "ID=?", new String[] {id});
    }
    //Add data to favorite or delete from favorite
    public void AddToFavorite(String id, int bool){
        ContentValues contents = new ContentValues();
        contents.put(isFavorite, bool);

        SQLiteDatabase sql = this.getWritableDatabase();
        sql.update(TableName, contents, "ID=?", new String[] {id});
    }
    //get favorite status
    public int FavoriteStatus(String id){
        String query = "SELECT * FROM " + TableName;
        SQLiteDatabase sql = this.getReadableDatabase();

        Cursor tables = null;
        if (sql != null) {
            tables = sql.rawQuery(query,null);
            while (tables.moveToNext()){
                if(tables.getString(0).equals(id)){
                    return tables.getInt(3);
                }
            }
        }
        return 0;
    }
    //Deleting a Data
    public  void DeleteData(String id){
        SQLiteDatabase sql = this.getWritableDatabase();
        sql.delete(TableName,"ID=?", new String[] {id});
    }
}
