package com.cetdhwani.bottlejump;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cet.magic75.dhwani.GameActivity;

import com.facebook.widget.FacebookDialog;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

class SendData extends AsyncTask<String,Void,Integer> {
String json,link;
int complete,login;
Context a;
Activity b;
String codefromweb; 
public SendData(String data,String url,int log,Context con,Activity A) {
	json="encryptionerror";
	MCrypt e=new MCrypt();
	try {
		json = e.bytesToHex(e.encrypt(data));
		

	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	link=url;
	complete=0;
	login=log;
	a=con;
	this.b=A;

	
	
	
}

	public SendData(String data,String url,int log,Context con) {
		
		
		MCrypt e=new MCrypt();
		String en;
		json="encryptionerror";
		try {
			json = e.bytesToHex(e.encrypt(data));
			
	
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		link=url;
		complete=0;
		login=log;
		a=con;
	}
	int type=0;
	HttpClient httpclient;
	HttpPost httppost;
	private ProgressDialog pDialog;
	@Override
		 protected Integer doInBackground(String...value) {
		// Create a new HttpClient and Post Header
		 
		   ConnectivityManager cm =(ConnectivityManager)a. getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		  if(!( netInfo != null && netInfo.isConnectedOrConnecting()))
		  {
			return -1;  
		  }

		httpclient = new DefaultHttpClient();
		HttpResponse response;
		 httppost = new HttpPost(link);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data",json));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs ,"UTF-8"));
			response = httpclient.execute(httppost);
		codefromweb=EntityUtils.toString(response.getEntity());
		
		
		
			return response.getStatusLine().getStatusCode();
			
			
		}
		catch (Exception e) {
			
			return -1;
		}
	}
	@Override
	protected void onPostExecute(Integer v) {
		
//		Toast.makeText(FacebookLogin.a, codefromweb, Toast.LENGTH_SHORT).show();
		if(v==200&&(login==0||login==1))
		{
			try {

				JSONObject  scoreweb=new JSONObject(codefromweb);
				Localstore A=new Localstore(a);
				JSONObject scorelocal=new JSONObject(A.getdata("ghjk"));
				Calendar c = Calendar.getInstance();
				int dayOfWeek = c.get(Calendar.DAY_OF_MONTH);
				
				JSONObject score=new JSONObject();
				if(scorelocal.getInt("score")>scoreweb.getInt("hightoday")&&scorelocal.getInt("day")==dayOfWeek)
				{
					score.put("token",A.getdata("asdf"));
					score.put("score",scorelocal.getInt("score") );
					score.put("day",dayOfWeek);
					if(A.getv()!=0)
					new SendData(score.toString(), "http://cetdhwani.com/bottlerun/update.php",2,a).execute();
				}
				else
				{
					
					score.put("token",A.getdata("asdf"));
					score.put("score",scoreweb.getInt("hightoday"));
					
					
					score.put("day",dayOfWeek);
					A.putdata("ghjk", score.toString());
				}
			} catch (JSONException e) {
				
				Localstore A=new Localstore(a);
				JSONObject score=new JSONObject();
				try {
				score.put("token",A.getdata("asdf"));
				
					score.put("score",0 );
					Calendar c = Calendar.getInstance();
					int dayOfWeek = c.get(Calendar.DAY_OF_MONTH);
					score.put("day",dayOfWeek);
				} catch (JSONException e1) {
					e1.printStackTrace();
					
				}

				A.putdata("ghjk", score.toString());
				
			}
			
		}
		
		
		if(login==0||login==1)
		{
			Intent i=new Intent(a,GameActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			b.finish();
			a.startActivity(i);	
			
		
			
		}
		if(v==-1)
		return;
		else if(login==3)
		{
			
			ArrayList<leader> leaderList=new ArrayList<leader>();
			try {
				JSONArray lis=new JSONArray(codefromweb);
		
				for(int i=0;i<lis.length();i++)
				{

					leader a=new leader();
					try
					{
					a.name=lis.getJSONObject(i).getString("Name");
					a.score=lis.getJSONObject(i).getString("hightoday");
					try
					{
						a.pos=lis.getJSONObject(i).getString("rank");
					}
					catch(Exception e)
					{
						a.pos=""+(i+1);
					}
					leaderList.add(a);
					}
					catch(JSONException e)
					{
						((MyAdapter)AfterGame.list1.getAdapter()).received(leaderList)	;	
					}
				}
((MyAdapter)AfterGame.list1.getAdapter()).received(leaderList)	;			
			} catch (JSONException e) {
				
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			
//			
							
			}
			finally
			{
				AfterGame.b1.setVisibility(View.VISIBLE);
				AfterGame.bar.setVisibility(View.GONE);
			}
			
		}
		else if(login==4)
		{
			ArrayList<String> coupons=new ArrayList<String>();
			try {
				JSONArray lis=new JSONArray(codefromweb);

				for(int i=0;i<lis.length();i++)
				{
		

					String a=new String(lis.getJSONObject(i).getString("code"));
					coupons.add(a);
				}
			}
			catch(Exception e)
			{
				
			}
			((WAlletMyAdapter)AfterGame.list2.getAdapter()).received(coupons)	;			
		}
		else if(login==5)
		{
			int vfw=0,mand=0;
			try {
				vfw = new JSONObject(codefromweb).getInt("version");
				mand=new JSONObject(codefromweb).getInt("mand");
				new Localstore(a).changeversion(vfw, mand);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			

			 

		}
			
		complete=v;
		Localstore A=new Localstore(a);
		JSONObject scorelocal;
		try {
			scorelocal = new JSONObject(A.getdata("ghjk"));
		
		Calendar c = Calendar.getInstance();
		int dayOfWeek = c.get(Calendar.DAY_OF_MONTH);
		
		JSONObject score=new JSONObject();
	if(scorelocal.getInt("day")!=dayOfWeek)
	{
		score.put("token",A.getdata("asdf"));
		
		score.put("score",0 );
		score.put("day",dayOfWeek);
		A.putdata("ghjk", score.toString());
	}
		
}catch (JSONException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

	

}}


