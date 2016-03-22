package com.xfzj.getbook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String NAME="downloadFile.db";
	private static DBHelper dbHelper;

	public static DBHelper getInsatance(Context context) {
		if (dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
		dbHelper = new DBHelper(context);
		return dbHelper;
	}

	public DBHelper(Context context) {
		super(context, NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table downloadfile (name varchar(255) primary key,path varchar(255) ,"
				+ "image  integer);");
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
