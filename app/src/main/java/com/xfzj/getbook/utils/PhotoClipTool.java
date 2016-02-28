package com.xfzj.getbook.utils;

public class PhotoClipTool {
//	public static Intent ClipFromUri(Uri ImageUri, int Size) {
//
//		Intent intent = Clip(Size);
//
//		intent.setDataAndType(ImageUri, "image/*");
//
//		return intent;
//	}
//
//	public static Intent ClipFromUri(Uri ImageUri, int SizeWidth, int SizeHeight) {
//		Intent intent = Clip(SizeWidth, SizeHeight);
//		intent.setDataAndType(ImageUri, "image/*");
//
//		return intent;
//	}
//
//	public static Intent ClipFromBitmap(Bitmap bitmap, int Size) {
//		Intent intent = Clip(Size);
//		intent.setType("image/*");
//		intent.putExtra("data", bitmap);
//
//		return intent;
//	}
//
//	public static Intent ClipFromBitmap(Bitmap bitmap, int SizeWidth,
//			int SizeHeight) {
//		Intent intent = Clip(SizeWidth, SizeHeight);
//		intent.setType("image/*");
//		intent.putExtra("data", bitmap);
//
//		return intent;
//	}
//
//	public static Intent Clip(int Size) {
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.putExtra("crop", "true");
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//
//		intent.putExtra("outputX", Size);
//		intent.putExtra("outputY", Size);
//		File file = new File(Constants.IMAGE_SAVE_PATH);
//
//		Uri imageUri = Uri.fromFile(file);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//		intent.putExtra("return-data", false);
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//		return intent;
//
//	}
//
//	public static Intent Clip(int SizeWidth, int SizeHeight) {
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.putExtra("crop", "true");
//		intent.putExtra("aspectX", SizeWidth);
//		intent.putExtra("aspectY", SizeHeight);
//		System.out.println("SizeWidth" + SizeWidth + "  SizeHeight"
//				+ SizeHeight);
//		intent.putExtra("outputX", SizeWidth);
//		intent.putExtra("outputY", SizeHeight);
//		File file = new File(Constants.IMAGE_SAVE_PATH);
//		Uri imageUri = Uri.fromFile(file);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//		intent.putExtra("return-data", false);
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//		return intent;
//
//	}
}
