package com.xfzj.getbook.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

public class PhotoClipTool {
	public static Intent ClipFromUri(Uri ImageUri, int Size,File file) {

		Intent intent = Clip(Size,file);

		intent.setDataAndType(ImageUri, "image/*");

		return intent;
	}

	public static Intent ClipFromUri(Uri ImageUri, int SizeWidth, int SizeHeight,File file) {
		Intent intent = Clip(SizeWidth, SizeHeight,file);
		intent.setDataAndType(ImageUri, "image/*");

		return intent;
	}

	public static Intent ClipFromBitmap(Bitmap bitmap, int Size,File file) {
		Intent intent = Clip(Size,file);
		intent.setType("image/*");
		intent.putExtra("data", bitmap);

		return intent;
	}

	public static Intent ClipFromBitmap(Bitmap bitmap, int SizeWidth,
			int SizeHeight,File file) {
		Intent intent = Clip(SizeWidth, SizeHeight,file);
		intent.setType("image/*");
		intent.putExtra("data", bitmap);

		return intent;
	}

	public static Intent Clip(int Size,File file) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", Size);
		intent.putExtra("outputY", Size);
		Uri imageUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		return intent;

	}

	public static Intent Clip(int SizeWidth, int SizeHeight,File file) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", SizeWidth);
		intent.putExtra("aspectY", SizeHeight);
		System.out.println("SizeWidth" + SizeWidth + "  SizeHeight"
				+ SizeHeight);
		intent.putExtra("outputX", SizeWidth);
		intent.putExtra("outputY", SizeHeight);
		Uri imageUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		return intent;

	}
}
