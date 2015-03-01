package com.cetdhwani.bottlejump;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;


public class Localstore {

SharedPreferences.Editor editor ;
SharedPreferences pref;
Context con;;
public Localstore(Context a)
{
	con=a;
	
	pref=a.getSharedPreferences("PrefDh", Context.MODE_PRIVATE);
	editor = pref.edit();
	createGameSettings();
	}
void createGameSettings(){
	if(!pref.contains("sound")){
		editor.putInt("sound", 1);
		editor.commit();	
	}
	if(!pref.contains("vibrate")){
		editor.putInt("vibrate", 1);
		editor.commit();			
	}
}
public boolean getSoundVal(){
	if(pref.getInt("sound",0)==1)
		return true;
	return false;
}

public boolean getVibrationVal(){
	if(pref.getInt("vibrate",0)==1)
		return true;
	return false;
}

public void toggleSound(){	
	editor.putInt("sound",1-pref.getInt("sound",0));
	editor.commit();	
}

public void toggleVib(){	
	editor.putInt("vibrate",1-pref.getInt("vibrate",0));
	editor.commit();	
}

void putdata(String a ,String b)
{
	MCrypt e=new MCrypt();
	String js="";
	try {
		js = e.bytesToHex(e.encrypt(b));
		

	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	editor.putString(a, js).commit();
	
}
String getdata(String a)
{
	String b="";
	MCrypt e=new MCrypt();
	b=pref.getString(a, ""); 
	String decrypted="";
	try {
		decrypted = new String( e.decrypt( b ) );
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	return decrypted;
	}
JSONObject score()
{
	JSONObject b=null;
	try {
		b = new JSONObject(getdata("ghjk"));
	
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	return b;
	}
public int getscore()
{
	JSONObject b=null;
	try {
		String de="";
		b=new JSONObject(getdata("ghjk"));
		
		return b.getInt("score");
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return 0;
	}
public  void changeversion(int v,int m)
{
	
	JSONObject b=null;
	try {
		b = new JSONObject();
		b.put("vz", v);
		b.put("mz", m);
		putdata("vbcx", b.toString());
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	}
public int getv()
{
	new SendData(pref.getString("asdf", ""), "http://cetdhwani.com/bottlerun/version.php",5,con).execute();

	try {
		JSONObject b=new JSONObject(getdata("vbcx"));
		int v=b.getInt("vz");
		int m=b.getInt("mz");
		try { 
	        PackageInfo pInfo =con.getPackageManager().getPackageInfo(con.getPackageName(), PackageManager.GET_META_DATA);
	       int  version = pInfo.versionCode;
	      
	       String url="";
	       if(v>version&&m==1)
	       {
	    	  try {
				     //Check whether Google Play store is installed or not:
				     con.getPackageManager().getPackageInfo("com.android.vending", 0);
			//	     Toast.makeText(con, "Please update application to continue using service",Toast.LENGTH_LONG).show();
				     url = "market://details?id=" +con.getPackageName();
				 } catch ( final Exception e ) {
				     url = "https://play.google.com/store/apps/details?id=" +con.getPackageName();
				 }


				 //Open the application page in Google Play store:
				 final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				 con.startActivity(intent);
	    	   return 0;
	    	   
	       } 
	       else if(v>version)
	    	   return 1;
	    
	    } catch (NameNotFoundException e1) {
	       
	    } 
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return 2;
	}
public int changehighscore(int sc)
{
	
	JSONObject b=null;
	Calendar c = Calendar.getInstance();
    int day=c.get(Calendar.DAY_OF_MONTH);
	

	try {
		b = new JSONObject(getdata("ghjk"));
		
		if(sc>b.getInt("score")||b.getInt("day")!=day)
		{
			b.put("token",getdata("asdf"));
			b.put("score", sc);
			b.put("day", day);
			putdata("ghjk",b.toString());
			
			new SendData(b.toString(), "http://cetdhwani.com/bottlerun/update.php",2,con).execute();
			
			return 1;
		}
		else
		{
			return 0;
		}
		
	
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return 0;
}
}