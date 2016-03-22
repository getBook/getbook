package com.xfzj.getbook.db;

import com.xfzj.getbook.common.DownloadFile;

import java.util.List;


public interface IDownLoadFileService {
	/**
	 * 插入文件
	 * 
	 * @param file
	 */
	public void insert(DownloadFile file);

	/**
	 * 获取所有文件
	 * 
	 * @return
	 */
	public List<DownloadFile> findAll();
	/**
	 * 获取指定文件
	 * 
	 * @return
	 */
	public boolean find(String name);

	/**
	 * 删除所有文件
	 */
	public void deleteAll();
	/**
	 * 删除指定文件
	 */
	public void delete(DownloadFile file);
	
}
