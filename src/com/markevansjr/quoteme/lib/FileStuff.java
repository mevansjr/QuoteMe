package com.markevansjr.quoteme.lib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class FileStuff {

	@SuppressWarnings("resource")
	public static Boolean storeStringFile(Context context, String filename, String content, Boolean external){
		try{
			File file;
			FileOutputStream fos;
			if(external){
				file = new File(context.getExternalFilesDir(null), filename);
				fos = new FileOutputStream(file);
			} else {
				fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			fos.write(content.getBytes());
			fos.close();
		} catch (IOException e){
			Log.e("WRITE ERROR", filename);
		}
		return true;
	}
	
	@SuppressWarnings("resource")
	public static Boolean storeObjectFile(Context context, String filename, Object content, Boolean external){
		try{
			File file;
			FileOutputStream fos;
			ObjectOutputStream oos;
			if(external){
				file = new File(context.getExternalFilesDir(null), filename);
				fos = new FileOutputStream(file);
			} else {
				fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			oos = new ObjectOutputStream(fos);
			oos.writeObject(content);
			oos.close();
			fos.close();
		} catch (IOException e){
			Log.e("WRITE ERROR", filename);
		}		
		return true;
	}
	
	@SuppressWarnings("resource")
	public static Boolean storeArrayFile(Context context, String filename, ArrayList<Map<String, String>> content, Boolean external){
		try{
			File file;
			FileOutputStream fos;
			ObjectOutputStream oos;
			if(external){
				file = new File(context.getExternalFilesDir(null), filename);
				fos = new FileOutputStream(file);
			} else {
				fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			oos = new ObjectOutputStream(fos);
			oos.writeObject(content);
			oos.close();
			fos.close();
		} catch (IOException e){
			Log.e("WRITE ERROR", filename);
		}		
		return true;
	}
	
	@SuppressWarnings("resource")
	public static String readStringFile(Context context, String filename, Boolean external){
		String content = "";
		try{
			File file;
			FileInputStream fin;
			if(external){
				file = new File(context.getExternalFilesDir(null), filename);
				fin = new FileInputStream(file);
			} else {
				file = new File(filename);
				fin = context.openFileInput(filename);
			}
			BufferedInputStream bin = new BufferedInputStream(fin);
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer contentBuffer = new StringBuffer();
			
			while((bytesRead = bin.read(contentBytes)) != -1){
				content = new String(contentBytes,0,bytesRead);
				contentBuffer.append(content);
			}
			content = contentBuffer.toString();
			fin.close();
		} catch (FileNotFoundException e){
			Log.e("READ ERROR", "FILE NOT FOUND " + filename);
		} catch (IOException e){
			Log.e("READ ERROR", "I/O ERROR");
		}
		return content;
	}
	
	@SuppressWarnings("resource")
	public static Object readObjectFile(Context context, String filename, Boolean external){
		Object content = new Object();
		try{
			File file;
			FileInputStream fin;
			if(external){
				file = new File(context.getExternalFilesDir(null), filename);
				fin = new FileInputStream(file);
			} else {
				file = new File(filename);
				fin = context.openFileInput(filename);
			}
			
			ObjectInputStream ois = new ObjectInputStream(fin);
			
			try{
				content = (Object) ois.readObject();
			} catch (ClassNotFoundException e){
				Log.e("READ ERROR", "INVALID JAVA OBJECT FILE");
			}
			ois.close();
			fin.close();
		} catch (FileNotFoundException e){
			Log.e("READ ERROR", "FILE NOT FOUND " + filename);
			return null;
		} catch (IOException e){
			Log.e("READ ERROR", "I/O ERROR");
		}
		return content;
	}
	
	@SuppressWarnings({ "unchecked", "resource" })
	public static ArrayList<Map<String, String>> readArrayFile(Context context, String filename, Boolean external){
		ArrayList<Map<String, String>> content = new ArrayList<Map<String, String>>();
		try{
			File file;
			FileInputStream fin;
			if(external){
				file = new File(context.getExternalFilesDir(null), filename);
				fin = new FileInputStream(file);
			} else {
				file = new File(filename);
				fin = context.openFileInput(filename);
			}
			
			ObjectInputStream ois = new ObjectInputStream(fin);
			
			try{
				content = (ArrayList<Map<String, String>>) ois.readObject();
			} catch (ClassNotFoundException e){
				Log.e("READ ERROR", "INVALID JAVA OBJECT FILE");
			}
			ois.close();
			fin.close();
		} catch (FileNotFoundException e){
			Log.e("READ ERROR", "FILE NOT FOUND " + filename);
			return null;
		} catch (IOException e){
			Log.e("READ ERROR", "I/O ERROR");
		}
		return content;
	}
}
