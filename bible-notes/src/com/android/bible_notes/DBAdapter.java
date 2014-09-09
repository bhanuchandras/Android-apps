package com.android.bible_notes;
import android.app.ListFragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    public static final String KEY_ROWID = "rowid";
    public static final String KEY_NAME = "tbl_name";
    public static final String KEY_EMAIL = "email";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "sqlite_master";
    private static final int  DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table if not exists notes (_id integer primary key autoincrement, "
        + "note_name text not null, note_body text not null);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {

        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    
    
    

  

	private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            /*try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e)  {
                e.printStackTrace();
            }*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
}

//---opens the database---
public DBAdapter open() throws SQLException
{
    db = DBHelper.getWritableDatabase();
    return this;
}

//---closes the database---
public void close()
{
    DBHelper.close();
}
//---insert a contact into the database---
public long insertContact(String name, String email)
{
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_NAME, name);
    initialValues.put(KEY_EMAIL, email);
    return db.insert(DATABASE_TABLE, null, initialValues);
}
//---deletes a particular contact---
public boolean deleteContact(long rowId)
{
    return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null)  > 0;
}

//---retrieves all the contacts---
public Cursor getAllContacts()
{
    return db.query(DATABASE_TABLE, new String[]   {KEY_ROWID, KEY_NAME}, KEY_ROWID + "<=" + 66, null, null, null, null);
}

public Cursor getAllNotes()
{
	return db.query("notes", new String[]   {"note_name", "note_body"}, null, null, null, null, null);
}

//---retrieves a particular contact---
public Cursor getContact(long rowId) throws SQLException
{
    Cursor mCursor =
            db.query(true, DATABASE_TABLE, new String[]  {KEY_ROWID,
            KEY_NAME},  KEY_ROWID + "=" + rowId,  null,
            null, null, null, null);
    if (mCursor != null) {
        mCursor.moveToFirst();
    }
    return mCursor;
}

public Cursor getbookChap(String book) throws SQLException
{
	Cursor mCursor =
            db.query(true, book, new String[]  {"max(chap)"},  null,  null,
            null, null, null, null);
	
	return(mCursor);
}

//---updates a contact---
public boolean updateContact(long rowId, String name, String email)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_EMAIL, email);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

public Cursor getverse(String bookname, String item) {
	// TODO Auto-generated method stub
	Cursor mCursor =
            db.query(true, bookname, new String[]  {"verse","text"},  "chap="+item,  null,
            null, null, null, null);
	
	return(mCursor);
}
public long insertNote(String title, String body)
{
    ContentValues initialValues = new ContentValues();
    initialValues.put("note_name", title);
    initialValues.put("note_body", body);
    return db.insert("notes", null, initialValues);
}

public Cursor getNote(String item) {
	// TODO Auto-generated method stub
	Cursor mCursor =
            db.query(true, "notes", new String[]  {"_id","note_name","note_body"},  "note_name='"+item+"'",  null,
            null, null, null, null);
	
	return(mCursor);
}

public long updateNote(long id,String title, String body)
{
	ContentValues args = new ContentValues();
	args.put("note_name", title);
	args.put("note_body", body);
    return db.update("notes", args, KEY_ROWID + "=" + id, null);
}

}
