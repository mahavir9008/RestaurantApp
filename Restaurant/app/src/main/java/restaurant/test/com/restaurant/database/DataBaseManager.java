package restaurant.test.com.restaurant.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseManager {

    // Customer table
    private static final String KEY_ROW_ID = "_id";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    public static final String KEY_CUSTOMER_NAME = "customer_name";
    private static final String CUSTOMER_TABLE = "CustomerInfo";

    // Booking Table
    public static final String KEY_BT_ROW_ID = "_id";
    public static final String KEY_BT_TABLE_NUMBER = "table_number";
    public static final String BOOKING_TABLE = "BookingTable";

    // DataBase Name
    private static final String TAG = "RestaurantDB";
    private static final String DATABASE_NAME = "Restaurant";
    private static final int DATABASE_VERSION = 1;

    // Customer Table Create Query
    private static final String CUSTOMER_TABLE_CREATE =
            "CREATE TABLE if not exists " + CUSTOMER_TABLE + " (" +
                    KEY_ROW_ID + " integer PRIMARY KEY autoincrement," +
                    KEY_CUSTOMER_NAME + "," +
                    KEY_CUSTOMER_ID + "," +
                    " UNIQUE (" + KEY_CUSTOMER_ID + "));";

    // Booking Table Create Query
    private static final String BOOKING_TABLE_CREATE =
            "CREATE TABLE if not exists " + BOOKING_TABLE + " (" +
                    KEY_BT_ROW_ID + " integer PRIMARY KEY autoincrement," +
                    KEY_BT_TABLE_NUMBER + "," +
                    " UNIQUE (" + KEY_BT_ROW_ID + "));";

    private final Context mCtx;
    private SQLiteDatabase mDb;

    public DataBaseManager(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     *
     * @return - instance of database
     * @throws SQLException - throw sql exception
     */
    public  DataBaseManager open() throws SQLException {
        DatabaseHelper mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * insert the customer data into the customer table
     * @param name - customer name
     * @param customer_ID - customer id
     */
    public void insertCustomerInfo(String name,
                                   String customer_ID) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CUSTOMER_NAME, name);
        initialValues.put(KEY_CUSTOMER_ID, customer_ID);
         mDb.insert(CUSTOMER_TABLE, null, initialValues);
    }

    /**
     * insert the booking status into the booking table
     * @param booking_status - booking status of the table
     */
    public void insertBookingInfo(String booking_status) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_BT_TABLE_NUMBER, booking_status);
         mDb.insert(BOOKING_TABLE, null, initialValues);
    }

    /**
     * delete the customer table
     */
    public void deleteCustomerData() {
        mDb.delete(CUSTOMER_TABLE, null, null);
    }

    public void deleteBookingTable() {
         mDb.delete(BOOKING_TABLE, null, null);
    }

    /**
     *
     * @param inputText - customer name
     * @return - filtered customer name
     * @throws SQLException - throw SQL exception
     */
    public Cursor fetchByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor;
        if (inputText == null || inputText.length() == 0) {
            mCursor = mDb.query(CUSTOMER_TABLE, new String[]{KEY_ROW_ID,
                            KEY_CUSTOMER_NAME, KEY_CUSTOMER_ID},
                    null, null, null, null, null);

        } else {
            mCursor = mDb.query(true, CUSTOMER_TABLE, new String[]{KEY_ROW_ID,
                            KEY_CUSTOMER_NAME, KEY_CUSTOMER_ID},
                    KEY_CUSTOMER_NAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     *
     * @return - all the customer name
     */
    public Cursor fetchAllCustomer() {
        Cursor mCursor = mDb.query(CUSTOMER_TABLE, new String[]{KEY_ROW_ID,
                        KEY_CUSTOMER_NAME, KEY_CUSTOMER_ID},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     *
     * @return - all the booking table status
     */
    public Cursor fetchAllBookingTableData() {
        Cursor mCursor = mDb.query(BOOKING_TABLE, new String[]{KEY_BT_ROW_ID,
                        KEY_BT_TABLE_NUMBER},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /***
         *
         * @param db - create the Customer and Booking Table
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CUSTOMER_TABLE_CREATE);
            db.execSQL(BOOKING_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + BOOKING_TABLE);
            onCreate(db);
        }
    }
}