package com.cetdhwani.bottlejump;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

class DownAd extends AsyncTask<String,Void,Integer> {

	String link="";
	Context a=null;
	DownAd(String url,Context con)
	{
		a=con;
		link=url;
	}
	public File getCacheFolder(Context context) {
		File cacheDir = null;
	        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	            cacheDir = new File(Environment.getExternalStorageDirectory(), "cachefolder");
	            if(!cacheDir.isDirectory()) {
	            	cacheDir.mkdirs();
	            }
	        }
	        
	        if(!cacheDir.isDirectory()) {
	            cacheDir = context.getCacheDir(); //get system cache folder
	        }
	        
		return cacheDir;
	}
	public File getDataFolder(Context context) {
		File dataDir = null;
	        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	        	dataDir = new File(Environment.getExternalStorageDirectory(), "myappdata");
	            if(!dataDir.isDirectory()) {
	            	dataDir.mkdirs();
	            }
	        }
	        
	        if(!dataDir.isDirectory()) {
	        	dataDir = context.getFilesDir();
	        }
	        
		return dataDir;
	}
	@Override
	protected Integer doInBackground(String... arg0) {
		
	try
	{
		URL wallpaperURL = new URL(link);
		URLConnection connection = wallpaperURL.openConnection();
		InputStream inputStream = new BufferedInputStream(wallpaperURL.openStream(), 10240);
		File cacheDir = getCacheFolder(a);
		File cacheFile = new File(cacheDir, "localFileName.jpg");
		FileOutputStream outputStream = new FileOutputStream(cacheFile);
				
		byte buffer[] = new byte[1024];
		int dataSize;
		int loadedSize = 0;
		       while ((dataSize = inputStream.read(buffer)) != -1) {
		           loadedSize += dataSize;
		           outputStream.write(buffer, 0, dataSize);
		       }
		           
		       outputStream.close();
	}
	catch(Exception e)
	{
		
	}
	    return -1;
	    
	}
	@Override
	protected void onPostExecute(Integer v) {
		
	
	}
	

}