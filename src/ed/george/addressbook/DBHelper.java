package ed.george.addressbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper
{

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		Log.d("DBHelper", "DBHelper Initialised");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("DBHelper", "DBHelper Initialised - OnCreate()");

		db.execSQL("CREATE TABLE addressBook (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT ," +
				"name VARCHAR(128) NOT NULL ," +
				"number VARCHAR(25) NOT NULL ," +
				"email VARCHAR(128)" +
				");");
		
		db.execSQL("INSERT INTO addressBook (name,number,email) " +
				   "VALUES " +
				   "('Martina', '(822) 671-6691','mart@mail.com');");
		db.execSQL("INSERT INTO addressBook (name,number,email) " +
				   "VALUES " +
				   "('Ed George' ,'07791887132','ed@mail.com');");
//		db.execSQL("INSERT INTO addressBook (name,number,email) " +
//				   "VALUES " +
//				   "('Craig','(855) 367-1782','mart@mail.com');");
//		db.execSQL("INSERT INTO addressBook (name,number,email) " +
//				   "VALUES " +
//				   "('Barry','(833) 383-7651','mart@mail.com');");
//		db.execSQL("INSERT INTO addressBook (name,number,email) " +
//				   "VALUES " +
//				   "('Edward','(855) 137-6049','mart@mail.com');");
		
		
		
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS addressBook");
		onCreate(db);
	}
	
}