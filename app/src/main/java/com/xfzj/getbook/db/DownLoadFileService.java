package com.xfzj.getbook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xfzj.getbook.common.DownloadFile;

import java.util.ArrayList;
import java.util.List;

public class DownLoadFileService implements IDownLoadFileService {
	private DBHelper helper;
	private SQLiteDatabase db;

	public DownLoadFileService(Context context) {
		if (helper != null) {
			helper.close();
			helper = null;
		}
		helper = DBHelper.getInsatance(context);
	}

	/**
	 * 插入文件
	 * 
	 * @param file
	 */
	@Override
	public void insert(DownloadFile file) {
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", file.name);
		values.put("path", file.path);
		values.put("image", file.image);
		db.insert("downloadfile", null, values);

	}

	@Override
	public List<DownloadFile> findAll() {
		db = helper.getReadableDatabase();
		List<DownloadFile> lists = new ArrayList<DownloadFile>();
		String[] columns = { "name", "path", "image" };
		Cursor cursor = db.query("downloadfile", columns, null, null, null,
				null, null);
		while (cursor.moveToNext()) {
			DownloadFile file = new DownloadFile();
			file.name = cursor.getString(cursor.getColumnIndex("name"));
			file.path = cursor.getString(cursor.getColumnIndex("path"));
			file.image = cursor.getInt(cursor.getColumnIndex("image"));
			lists.add(file);
		}
		cursor.close();
		return lists;
	}

	@Override
	public void deleteAll() {
		db = helper.getReadableDatabase();
		db.delete("downloadfile", null, null);
	}

	@Override
	public void delete(DownloadFile file) {
		db = helper.getReadableDatabase();
		String whereClause = "name=?";
		String[] whereArgs = { file.name };
		db.delete("downloadfile", whereClause, whereArgs);
	}

	@Override
	public boolean find(String name) {
		Cursor cursor=null;
		try {
		
			db = helper.getReadableDatabase();
			List<DownloadFile> lists = new ArrayList<DownloadFile>();
			String[] columns = {"name", "path", "image"};
			String selection = "name=?";
			String[] selectionArgs = {name};
			 cursor = db.query("downloadfile", columns, selection,
					selectionArgs, null, null, null);
			if (cursor.moveToFirst())
				return true;
			return false;
		} catch (Exception e) {
			return false;
		}finally {
			if (null != cursor) {
				cursor.close();
			}
		}
	}
}
