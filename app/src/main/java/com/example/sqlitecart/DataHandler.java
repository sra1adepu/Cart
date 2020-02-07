package com.example.sqlitecart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHandler extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Cart";

    // Contacts table name
    private static final String TABLE_CONTACTS = "Orders";


    // Contacts Table Columns names

//        private static final String USER_ID = "user_id";
//    private static final String USER_PASSWORD = "user_password";
    private static final String PRODUCT_SN = "serialnumber";
    private static final String PRODUCT_NAME = "productname";
    private static final String PRODUCT_QUANTITY = "productquantity";
    private static final String PRODUCT_PRICE = "productprice";
    private static final String PRODUCT_ID = "productid";
    private static final String PRODUCT = "product";


    private static final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE " + TABLE_CONTACTS + " ADD COLUMN " + PRODUCT_ID + " string;";
    private static final String DATABASE_ALTER_TEAM_2 = "ALTER TABLE " + TABLE_CONTACTS + " ADD COLUMN " + PRODUCT + " INTEGER DEFAULT 0;";


    public DataHandler(Context context) {
       // super(new DatabaseContext(context), DATABASE_NAME, null, DATABASE_VERSION);// by this we can create in Externalstorge in our device by this we can see db using sqliteManager app
        super(context, DATABASE_NAME, null, DATABASE_VERSION);// by this we can create db inside application memory
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + USER_ID + " TEXT," + USER_PASSWORD + " TEXT,"
//                + PRODUCT_SN + " INTEGER," + PRODUCT_NAME + " TEXT,"
//                + PRODUCT_QUANTITY + " TEXT," + PRODUCT_PRICE + " TEXT" + ")";

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + PRODUCT_SN + " INTEGER," + PRODUCT_NAME + " TEXT,"
                + PRODUCT_QUANTITY + " TEXT," + PRODUCT_PRICE + " TEXT" + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
// Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);



        // Create tables again
        onCreate(sqLiteDatabase);

        // we can add columns to existing db
//        if (i < i1)
//        {
//            sqLiteDatabase.execSQL(DATABASE_ALTER_TEAM_1);
//        }
        // we can add columns to existing db and default value is fixed for all rows is 0
//        if (i < i1){
//            sqLiteDatabase.execSQL(DATABASE_ALTER_TEAM_2);
//        }

    }

    public Cursor getAllContacts() {

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }

    public Cursor getProductName(String name) {

        String query = "SELECT * FROM " + TABLE_CONTACTS + " Where " + PRODUCT_NAME + "= '" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        return cursor;


    }

    public void update(String name, String count) {
        String sql = "UPDATE " + TABLE_CONTACTS + " SET " + PRODUCT_QUANTITY + " = '" + count + "' WHERE " + PRODUCT_NAME + " = '" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();


        db.execSQL(sql);

    }

    public void delete(String name) {
        String sql = "DELETE FROM " + TABLE_CONTACTS  + " WHERE " + PRODUCT_NAME + " = '" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();


        db.execSQL(sql);

    }

    public void insert(int i, String name, int count, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_SN, i);
        contentValues.put(PRODUCT_NAME, name);
        contentValues.put(PRODUCT_QUANTITY, count);
        contentValues.put(PRODUCT_PRICE, price);

        db.insert(TABLE_CONTACTS, null, contentValues);
        db.close();
    }
}
