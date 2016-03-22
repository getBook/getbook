package com.xfzj.getbook.db;

import android.content.Context;

import com.xfzj.getbook.common.DownloadFile;

import java.util.List;

public class DownLoadFileManager {
	private DownLoadFileService service;

	public DownLoadFileManager(Context context) {
		service = new DownLoadFileService(context);
	}

	/**
	 * 插入文件
	 * 
	 * @param file
	 */
	public void insert(DownloadFile file) {
		try {
			service.insert(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取下载的文件
	 * 
	 * @return
	 */
	public List<DownloadFile> findAll() {
		List<DownloadFile> lists = null;
		try {
			lists = service.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lists;
	}

	/**
	 * 获取指定的下载的文件是否存在
	 * 
	 * @return
	 */
	public boolean find(String name) {

		try {
			return service.find(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 删除指定文件
	 */
	public void delete(DownloadFile file) {
		try {
			service.delete(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除所有文件
	 */
	public void delete() {
		try {
			service.deleteAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
