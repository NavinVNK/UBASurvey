package ubasurvey.nawin.com.ubasurvey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "ubasurvey";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_UBAID = "ubaid";
    public static final String COLUMN_HOUSEHOLDID = "householdid";
    public static final String COLUMN_VILLAGE = "village";
    public static final String COLUMN_GRAM = "grampanchayat";
    public static final String COLUMN_STREET = "street";
    public static final String COLUMN_WARDNO = "wradno";
    public static final String COLUMN_BLOCK = "block";
    public static final String COLUMN_DIST = "district";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_HEAD = "nameofthehead";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LONG = "longitute";
    public static final String COLUMN_USER = "username";



    private static final String DATABASE_NAME = "uba.db";
    private static final int DATABASE_VERSION = 1;
    private String[] allColumns = { COLUMN_ID,
            COLUMN_HOUSEHOLDID,COLUMN_VILLAGE };

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_UBAID+ " text not null UNIQUE,"+COLUMN_HOUSEHOLDID
            + " text not null DEFAULT '',"+COLUMN_VILLAGE+" text not null default '',"+COLUMN_GRAM+" text not null default '',"+COLUMN_STREET+" text not null default '',"+COLUMN_WARDNO+" text not null default '',"
    +COLUMN_BLOCK+" text not null default '',"+COLUMN_DIST+" text not null default '',"+COLUMN_STATE+" text not null default '',"+COLUMN_HEAD+" text not null default '',"+COLUMN_GENDER+" text not null default '',"+COLUMN_LAT+" text not null default '',"+COLUMN_LONG+" text not null default '',"+COLUMN_USER+" text not null default '')";
    private static DatabaseHelper sInstance;
    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);

       /* Log.w(DatabaseHelper.class.getName(),
                "Creating database for uba" );
        ContentValues values = new ContentValues();
        values.put(COLUMN_UBAID,"t67");
        values.put(COLUMN_HOUSEHOLDID, "10"); // Contact Name
        values.put(COLUMN_VILLAGE, "Orathur"); // Contact Phone Number
        values.put(COLUMN_GRAM, "Orath"); // Contact Phone Number

        // Inserting Row
        database.insert(TABLE_NAME, null, values);*/


    }

    public List<Record> getAllData() {

        List<Record> RecordList = new ArrayList<>();


        String RECORDS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        TABLE_NAME
                        );

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(RECORDS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    String ubaid = cursor.getString(1);
                    String nameofHead = cursor.getString(10);
                    String gramPanchayat = cursor.getString(4);
                    Record record = new Record(ubaid, nameofHead, gramPanchayat);
                    RecordList.add(record);


                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("Basicinfo", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return RecordList;
    }
    public List<FullRecord> getFullData() {


        List<FullRecord> RecordList = new ArrayList<>();

        String RECORDS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        TABLE_NAME
                );

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(RECORDS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    String ubaid = cursor.getString(1);
                    String householdid = cursor.getString(2);
                    String village = cursor.getString(3);
                    String grampanchayat = cursor.getString(4);
                    String street = cursor.getString(5);
                    String wardno = cursor.getString(6);
                    String block= cursor.getString(7);
                    String district = cursor.getString(8);
                    String state = cursor.getString(9);
                    String nameofthehead = cursor.getString(10);
                    String gender = cursor.getString(11);
                    String latitude = cursor.getString(12);
                    String longitude = cursor.getString(13);
                    String username = cursor.getString(14);

                    FullRecord record = new FullRecord(ubaid,householdid,village,grampanchayat,street,wardno,block,district,state,nameofthehead,gender,latitude,longitude,username);
                    RecordList.add(record);


                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("Basicinfo", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return RecordList;
    }
public String getRecordData(String ubaid)
{
    JSONObject obj=null;
    String usersSelectQuery = String.format("SELECT * FROM %s WHERE %s = ?",
             TABLE_NAME, COLUMN_UBAID);
    SQLiteDatabase db = getReadableDatabase();
    //db.beginTransaction();
    Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(ubaid)});
    try {
        if (cursor.moveToFirst()) {
            Map<String, String> basicInfoMap = new HashMap<String, String>();
            basicInfoMap.put("ubaid", cursor.getString(1));
            basicInfoMap.put("householdid",cursor.getString(2));
            basicInfoMap.put("village", cursor.getString(3));
            basicInfoMap.put("grampanchayat", cursor.getString(4));
            basicInfoMap.put("street",cursor.getString(5));
            basicInfoMap.put("wardno", cursor.getString(6));
            basicInfoMap.put("block",cursor.getString(7));
            basicInfoMap.put("district", cursor.getString(8));
            basicInfoMap.put("state", cursor.getString(9));
            basicInfoMap.put("nameofthehead",cursor.getString(10));
            basicInfoMap.put("gender",cursor.getString(11));
            basicInfoMap.put("latitude",cursor.getString(12));
            basicInfoMap.put("longitude",cursor.getString(13));
            obj=new JSONObject(basicInfoMap);

           // db.setTransactionSuccessful();
        }
    } finally {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
    return obj.toString();
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String basicInfo[]) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Log.d("Basicinfo", basicInfo[12]);
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_UBAID, basicInfo[12]);
            contentValues.put(COLUMN_HOUSEHOLDID, basicInfo[11]);
           contentValues.put(COLUMN_VILLAGE, basicInfo[5]);
             contentValues.put(COLUMN_GRAM, basicInfo[2]);
            contentValues.put(COLUMN_STREET, basicInfo[8]);
            contentValues.put(COLUMN_WARDNO, basicInfo[3]);
            contentValues.put(COLUMN_BLOCK, basicInfo[1]);
            contentValues.put(COLUMN_DIST, basicInfo[0]);
            contentValues.put(COLUMN_STATE, basicInfo[4]);
            contentValues.put(COLUMN_HEAD, basicInfo[9]);
            contentValues.put(COLUMN_GENDER, basicInfo[10]);
           contentValues.put(COLUMN_LAT, basicInfo[13]);
            contentValues.put(COLUMN_LONG, basicInfo[14]);
            contentValues.put(COLUMN_USER, basicInfo[15]);
            db.insertOrThrow(TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();

        }
        catch (Exception e) {
            Log.d("Basicinfo", "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }
    public boolean updateData(String basicInfo[]) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_UBAID, basicInfo[12]);
        contentValues.put(COLUMN_HOUSEHOLDID, basicInfo[11]);
        contentValues.put(COLUMN_VILLAGE, basicInfo[5]);
        contentValues.put(COLUMN_GRAM, basicInfo[2]);
        contentValues.put(COLUMN_STREET, basicInfo[8]);
        contentValues.put(COLUMN_WARDNO, basicInfo[3]);
        contentValues.put(COLUMN_BLOCK, basicInfo[1]);
        contentValues.put(COLUMN_DIST, basicInfo[0]);
        contentValues.put(COLUMN_STATE, basicInfo[4]);
        contentValues.put(COLUMN_HEAD, basicInfo[9]);
        contentValues.put(COLUMN_GENDER, basicInfo[10]);
        contentValues.put(COLUMN_LAT, basicInfo[13]);
        contentValues.put(COLUMN_LONG, basicInfo[14]);
        contentValues.put(COLUMN_USER, basicInfo[15]);
        long result = db.update(TABLE_NAME,contentValues,COLUMN_UBAID+ " = ?",
                new String[] { String.valueOf(basicInfo[16]) });
        if(result == -1)
            return false;
        else
            return true;
    }


    public void deleteRecord(String ubaid) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_NAME, "ubaid=?", new String[] {ubaid});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("Basicinfo", "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }


}