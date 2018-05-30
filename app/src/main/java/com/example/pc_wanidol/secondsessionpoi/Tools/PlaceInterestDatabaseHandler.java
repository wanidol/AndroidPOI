package com.example.pc_wanidol.secondsessionpoi.Tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pc_wanidol.secondsessionpoi.Models.PlaceInterest;
import java.util.ArrayList;

/**
 * Created by PC-Wanidol on 09/08/2017.
 */

public class PlaceInterestDatabaseHandler extends SQLiteOpenHelper {

    Context mContext = null;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "DB_POI";

    // Contacts table name
    private static final String TABLE_POI = "TB_POI";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PLACE_NAME = "placename";
    private static final String KEY_WEB_URL = "weburl";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    private String CREATE_POI_TABLE = "CREATE TABLE " + TABLE_POI + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_PLACE_NAME + " TEXT, "
            + KEY_LATITUDE + " REAL, "
            + KEY_LONGITUDE + " REAL)";

    private String insert_beg = " INSERT INTO " + TABLE_POI + "("
            + KEY_PLACE_NAME + ", "
            + KEY_LATITUDE + ", "
            + KEY_LONGITUDE + ") VALUES ";


    public PlaceInterestDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        Log.d("Database Operation","Table created");
    }


    @Override

    public void onCreate(SQLiteDatabase sdb) {

        sdb.execSQL(CREATE_POI_TABLE);

        String insert_end = "(\"le lion de Belfort\" ,47.636639,6.864612);";
        sdb.execSQL(insert_beg + insert_end);


        insert_end = "(\"le théâtre de Mandeure\" ,47.4489926, 6.7939315);";
        sdb.execSQL(insert_beg + insert_end);


        insert_end = "(\"le château de Montbéliard\" ,47.509392, 6.800944);";
        sdb.execSQL(insert_beg + insert_end);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sdb, int i, int i1) {

        // Drop older table if existed
        sdb.execSQL("DROP TABLE IF EXISTS " + TABLE_POI);

        // Create tables again
        onCreate(sdb);

    }

    public boolean addData(String poi,float latitude,float longitude){
        //Insert data into DB
        SQLiteDatabase SQ = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_PLACE_NAME,poi);
        cv.put(KEY_LATITUDE,latitude);
        cv.put(KEY_LONGITUDE,longitude);
        long result = SQ.insert(TABLE_POI,null,cv);
        Log.d("Database Operation","One row inserted");
        if (result == -1){
            return false;

        }
        else{
            return true;
        }
    }


    public ArrayList<PlaceInterest> getPOI() {
        ArrayList<PlaceInterest> PoiList = new ArrayList<PlaceInterest>();
        // Select All Query

        String selectQuery = "SELECT  * FROM " + TABLE_POI + " ORDER BY " + KEY_ID ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PlaceInterest poi = new PlaceInterest();
                poi.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                poi.setPlacename(cursor.getString(cursor.getColumnIndex(KEY_PLACE_NAME)));
                poi.setLatitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE))));
                poi.setLongitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE))));
                // Adding poi to list
                PoiList.add(poi);

            } while (cursor.moveToNext());
        }
        db.close();
        // return Place interest list
        return PoiList;
    }

    public PlaceInterest getPlaceInterest(int id) {
        PlaceInterest poi = new PlaceInterest();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POI, new String[] { KEY_ID,
                        KEY_PLACE_NAME,KEY_LATITUDE,KEY_LONGITUDE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        poi.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
        poi.setPlacename(cursor.getString(cursor.getColumnIndex(KEY_PLACE_NAME)));
        poi.setLatitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE))));
        poi.setLongitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE))));

        return poi;
    }
}
