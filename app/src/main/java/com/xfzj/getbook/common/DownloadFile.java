package com.xfzj.getbook.common;

public class DownloadFile {
	public String path;
	public String name;
	/**
	 * 0 word
	 * 1 xls
	 * 2 ppt
	 * 3 pic
	 * 4 other
	 */
	public int image;

	/**
	 * @param path
	 * @param name
	 * @param image
	 */
	public DownloadFile(String path, String name, int image) {
		this.path = path;
		this.name = name;
		this.image = image;
	}

	/**
 * 
 */
	public DownloadFile() {
	}

	

}
