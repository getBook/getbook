package com.xfzj.getbook.common;

public class DownloadFile {
	public String path;
	public String name;
	
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
