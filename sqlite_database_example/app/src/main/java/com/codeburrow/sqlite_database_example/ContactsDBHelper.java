package com.codeburrow.sqlite_database_example;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codeburrow.sqlite_database_example.DatabaseContract.ContactsEntry;

import java.util.ArrayList;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jun/20/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
/**
 * ContactsDBHelper manages a local database for contacts data.
 * It handles all database CRUD (Create, Read, Update, Delete) operations.
 */
public class ContactsDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = ContactsDBHelper.class.getSimpleName();

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "contacts.db";

    public ContactsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // The SQL statement to create the Contacts table.
        final String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE" +
                ContactsEntry.TABLE_NAME + " (" +
                ContactsEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                ContactsEntry.COLUMN_NAME + " TEXT, " +
                ContactsEntry.COLUMN_PHONE_NUMBER + " TEXT" + " );";
        // Execute the SQL statement.
        sqLiteDatabase.execSQL(SQL_CREATE_CONTACTS_TABLE);
    }

    /**
     * Called when the database needs to be upgraded.
     *
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop older table, if it exists.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContactsEntry.TABLE_NAME);
        // Create table again.
        onCreate(sqLiteDatabase);
    }

    /*
     * All CRUD (Create, Read, Update, Delete) Operations.
     */

    /**
     * Insert NEW Record-Contact into the SQLite Database.
     *
     * @param contact The ContactDAO to be inserted.
     * @return The row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long createContact(ContactDAO contact) {
        return -1;
    }

    /**
     * Read SINGLE Record-Contact from the SQLiteDatabase.
     *
     * @param id The ID of the requested contact.
     * @return A Contact Data Access Object.
     */
    public ContactDAO readContact(int id) {
        return new ContactDAO();
    }

    /**
     * Update SINGLE Record-Contact from the SQLiteDatabase.
     *
     * @param contact The ContactDAO to be updated.
     * @return The number of rows affected.
     */
    public int updateContact(ContactDAO contact) {
        return 0;
    }

    /**
     * Delete SINGLE Record-Contact from the SQLiteDatabase.
     * To remove all rows and get a count pass "1" as the whereClause.
     *
     * @param contact
     * @return The number of rows affected if a whereClause is passed in, 0 otherwise.
     */
    public int deleteContact(ContactDAO contact) {
        return 0;
    }

    /**
     * Read ALL Records-Contacts from the SQLiteDatabase.
     *
     * @return A List with the ContactDAO objects.
     */
    public ArrayList<ContactDAO> readAllContacts() {
        return new ArrayList<>();
    }

    /**
     * Count the Records-Contacts of the SQLiteDatabase.
     *
     * @return The number of rows - stored contacts.
     */
    public int countContacts() {
        return 0;
    }
}