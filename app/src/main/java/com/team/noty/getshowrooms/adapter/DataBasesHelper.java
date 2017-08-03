package com.team.noty.getshowrooms.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by user on 24.03.16.
 */
public class DataBasesHelper extends SQLiteOpenHelper {

    Context context;

    private final String TAG = "DatabaseHelperClass";
    private static final int databaseVersion = 1;
    private static final String databaseName = "Favorite";
    private static final String TABLE_SHOWROOMS = "ShowRooms";

    private static final String ID = "id";
    private static final String NAME_SHOWROOMS = "name_showrooms";
    private static final String RATING = "rating";
    private static final String ADDRESS = "adress";
    private static final String WORK_TIME = "work_time";



    ArrayList IdList = new ArrayList();

    private static final String CREATE_TABLE_SHOWROOMS = "CREATE TABLE " + TABLE_SHOWROOMS + "("
            + ID + " TEXT ,"
            + NAME_SHOWROOMS + " TEXT, "
            + RATING + " TEXT, "
            + ADDRESS + " TEXT, "
            + WORK_TIME + " TEXT) ";



    public DataBasesHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SHOWROOMS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOWROOMS);

        onCreate(sqLiteDatabase);
    }

    public void insertShowRooms(String id, String name_showrooms, String rating, String address, String work_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
            values.put(ID, id);
            values.put(NAME_SHOWROOMS, name_showrooms);
            values.put(RATING, rating);
            values.put(ADDRESS, address);
            values.put(WORK_TIME, work_time);
            db.insert(TABLE_SHOWROOMS, null, values);
        db.close();
    }

    public ArrayList getidRow() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_SHOWROOMS, null, null, null, null, null, null);
        if(c!=null&&c.moveToFirst()){
            do{
                Log.d("MyLog", String.valueOf(c));
                String id = c.getString(c.getColumnIndexOrThrow (ID));
                IdList.add(id);

            }while(c.moveToNext());
        }
        return IdList;
    }


    public ShowRoomsHelper getShowRooms(String productid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor2 = db.query(TABLE_SHOWROOMS,
                new String[] {ID, NAME_SHOWROOMS, RATING, ADDRESS, WORK_TIME},ID
                        +" LIKE '"+productid+"%'", null, null, null, null);

        ShowRoomsHelper showRoomsHelper = new ShowRoomsHelper();
        if (cursor2.moveToFirst()) {
            do {
                showRoomsHelper.setId(cursor2.getString(0));
                showRoomsHelper.setName_showrooms(cursor2.getString(1));
                showRoomsHelper.setRating(cursor2.getString(2));
                showRoomsHelper.setAddress(cursor2.getString(3));
                showRoomsHelper.setWork_time(cursor2.getString(4));

            } while (cursor2.moveToNext());
        }
        cursor2.close();
        db.close();
        return showRoomsHelper;
    }


}