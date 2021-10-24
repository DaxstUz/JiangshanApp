package com.jiangshan.knowledge.uitl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;


/**
 * 文件简单加密
 * 
 * @author xc.li
 *
 */
public class FileUtil {

	/**
	 * 将文件简单加密
	 * 
	 * @param in
	 *            文件输入流
	 * @param dircPath
	 *            保存路径，文件名可以不用后缀
	 * @param xor
	 *            加密密码，如123;8进制，16进制都可以
	 * @return
	 */
	public static boolean encodeFile(InputStream in, String dircPath, int xor) {
		boolean success = true;
		int b;
		try {
			BufferedInputStream bis = new BufferedInputStream(in);
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(dircPath));
			while ((b = bis.read()) != -1) {
				bos.write(b ^ xor);
			}
			bis.close();
			bos.close();
		} catch (IOException e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * 解密加密后的图片文件
	 * 
	 * @param filePath
	 *            密文路径
	 * @param xor
	 *            解密密码（同加密密码）
	 * @return
	 */
	public static Bitmap decodeImageFile(String filePath, int xor) {
		int b;
		Bitmap bitmap = null;
		File f = new File(filePath);
		try {
			if (f.exists() && f.isFile()) {
				FileInputStream in = new FileInputStream(f);
				BufferedInputStream bis = new BufferedInputStream(in);
				List<Byte> list = new ArrayList<Byte>();
				while ((b = bis.read()) != -1) {
					list.add(Byte.valueOf((byte) (b ^ xor)));
				}
				in.close();
				bis.close();
				byte[] byteArr = new byte[list.size()];
				for (int i = 0; i < list.size(); i++) {
					byteArr[i] = list.get(i);
				}
				bitmap = BitmapFactory.decodeByteArray(byteArr, 0, list.size());
			} else {// 图片不存在

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 将assets下的文件按行读取到map 文件内容为按行存储 key=value 格式
	 * 
	 * @param filePath
	 * @param context
	 * @return
	 */
	public static HashMap<String, Object> readFileToMap(String filePath,
			Context context) {
		AssetManager assetMag = context.getAssets();
		HashMap<String, Object> keyMap = new HashMap<String, Object>();
		try {
			InputStream in = assetMag.open(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] keyVal = line.trim().split("=");
				if (keyVal.length == 2) {
					keyMap.put(keyVal[0].trim(), keyVal[1].trim());
				} else {
					keyMap.put(keyVal[0].trim(), "");
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return keyMap;

	}

	/**
	 * 删除单个文件
	 * 
	 * @param filePath
	 *            被删除文件的文件名
	 * @return 文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 删除文件夹以及目录下的文件
	 * 
	 * @param filePath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String filePath) {
		boolean flag = false;
		// 如果filePath不以文件分隔符结尾，自动添加文件分隔符
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		File dirFile = new File(filePath);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		File[] files = dirFile.listFiles();
		// 遍历删除文件夹下的所有文件(包括子目录)
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				// 删除子文件
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} else {
				// 删除子目录
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前空目录
		return dirFile.delete();
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 *
	 * @param filePath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public boolean DeleteFolder(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		} else {
			if (file.isFile()) {
				// 为文件时调用删除文件方法
				return deleteFile(filePath);
			} else {
				// 为目录时调用删除目录方法
				return deleteDirectory(filePath);
			}
		}
	}

	/**
	 * 把章节信息保存到本地文件
	 * 
	 * @param dir
	 * @param content
	 */
	public static void writeAdatpterToFile(String dir, String content) {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}

		try {
			FileWriter filerWriter = new FileWriter(new File(file, "temp.txt"),
					false);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			BufferedWriter bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(content);
			bufWriter.newLine();
			bufWriter.close();
			filerWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文件
	 * 
	 * @param dir
	 *            文件路径
	 * @return
	 */
	public static String getStringFormFile(String dir) throws Exception {
		byte Buffer[] = new byte[1024];
		// 得到文件输入流
		File file = new File(dir);
		FileInputStream in = null;
		ByteArrayOutputStream outputStream = null;
		try {
			in = new FileInputStream(file);
			// 创建一个字节数组输出流
			outputStream = new ByteArrayOutputStream();
			// 读出来的数据首先放入缓冲区，满了之后再写到字符输出流中
			int len = in.read(Buffer);
			while(len>0){
				outputStream.write(Buffer, 0, len);
				len = in.read(Buffer);
			}
			// 把字节输出流转String
			return new String(outputStream.toByteArray());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}


	//从assets 文件夹中获取文件并读取数据
	public static String getFromAssets(Context context, String fileName) {
		String result = "";
		try {
			InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取系统图片路径
	 *
	 * @param activity
	 * @param uri
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getImagePath(Activity activity, Uri uri) {
		String path = null;
		boolean isKITKAT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		try {
			if (isKITKAT && DocumentsContract.isDocumentUri(activity, uri)) {
				path = getImagePathInHeightApi(activity, uri);
			} else {
				path = getImagePathInLowApi(activity, uri);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * 4.4系统以上
	 *
	 * @param activity
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private static String getImagePathInHeightApi(Activity activity, Uri uri) throws Exception {
		String path = null;
		String wholeID = DocumentsContract.getDocumentId(uri);
		String id = wholeID.split(":")[1];
		String[] column = {MediaStore.Images.Media.DATA};
		String sel = MediaStore.Images.Media._ID + "=?";
		Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);
		;

		int columnindex = cursor.getColumnIndex(column[0]);
		if (cursor.moveToFirst()) {
			path = cursor.getString(columnindex);
		}
		cursor.close();
		return path;
	}

	/**
	 * 4.4系统以下
	 *
	 * @param activity
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	private static String getImagePathInLowApi(Activity activity, Uri uri) throws Exception {
		Cursor cursor = null;
		String path = null;

		try {
			String[] projection = {MediaStore.Images.Media.DATA};
			cursor = activity.getContentResolver().query(uri, projection, null, null, null);
			int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(actual_image_column_index);
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return path;
	}

	
}
