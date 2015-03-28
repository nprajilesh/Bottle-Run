package com.cetdhwani.bottlejump;

import java.sql.Timestamp;
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

import com.cetdhwani.scenes.MenuScene;
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

public class SendData extends AsyncTask<String,Void,Integer> {
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

	int type=0;
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

			
				Localstore A=new Localstore(a);
				JSONObject scorelocal=new JSONObject(A.getdata("ghjk"));
				Calendar c = Calendar.getInstance();
				int dayOfWeek = c.get(Calendar.DAY_OF_MONTH);
				JSONObject score=new JSONObject(A.getdata("ghjk"));
				if(dayOfWeek!=scorelocal.getInt("day"))
				{
					score.put("token",A.getdata("asdf"));
					score.put("score",0);
					score.put("day",dayOfWeek);
					java.util.Date date=new java.util.Date();
					score.put("ts",new Timestamp(date.getTime()) );
					score.put("x", scorelocal.getInt("x"));
					score.put("y",scorelocal.getInt("y") );
					score.put("z", scorelocal.getInt("z"));
				}
				else
				{
					score=scorelocal;
					score.put("token", A.getdata("asdf"));
					
				}
					if(A.getv()!=0)
					{
						Log.i("request",score.toString());
					new SendData(score.toString(), "http://cetdhwani.com/bottlerun/update.php",2,a).execute();
					
					}
					
					/*score.put("token",A.getdata("asdf"));
					score.put("score",scoreweb.getInt("hightoday"));
					
					java.util.Date date=new java.util.Date();
					score.put("ts",new Timestamp(date.getTime()) );
					score.put("x", scorelocal.getInt("x"));
					score.put("y",scorelocal.getInt("y") );
					score.put("z", scorelocal.getInt("z"));
					score.put("day",dayOfWeek);
					A.putdata("ghjk", score.toString());*/
				
			} catch (JSONException e) {
				
				Localstore A=new Localstore(a);
				JSONObject score=new JSONObject();
				try {
				score.put("token",A.getdata("asdf"));
				
					score.put("score",0 );
					Calendar c = Calendar.getInstance();
					int dayOfWeek = c.get(Calendar.DAY_OF_MONTH);
					score.put("day",dayOfWeek);
					java.util.Date date=new java.util.Date();
					score.put("ts",new Timestamp(date.getTime()) );
					score.put("x", 0);
					score.put("y",0);
					score.put("z", 0);
				} catch (JSONException e1) {
					e1.printStackTrace();
					
				}

				A.putdata("ghjk", score.toString());
				
			}
			
		}
		
		
//		if(login==0||login==1)
//		{if(FacebookLogin.progress!=null)
//			FacebookLogin.progress.setVisibility(View.GONE);
//			/*Intent i=new Intent(a,GameActivity.class);
//			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			
//			b.finish();
//			a.startActivity(i);	*/
//			
//		
//			
//		}
		if(v==-1)
		return;
		else if(login==3)
		{
			
			try {
				JSONObject all_leader_board=new JSONObject(codefromweb);
				JSONArray lis=new JSONArray(all_leader_board.getString("leaderboard"));
				JSONArray winners=new JSONArray(all_leader_board.getString("winners"));
				JSONArray hscores=new JSONArray(all_leader_board.getString("highscores"));

			    MyAdapter leader_adapter=new MyAdapter(MenuScene.activity.getApplicationContext(),JsonToList(lis));
			    MyAdapter winner_adapter=new MyAdapter(MenuScene.activity.getApplicationContext(),JsonToList(winners));
			    MyAdapter hscore_adapter=new MyAdapter(MenuScene.activity.getApplicationContext(),JsonToList(hscores));
			    
			    MenuScene.winnerList.setAdapter(winner_adapter)	;	
			    MenuScene.ldrList.setAdapter(leader_adapter)	;	
			    MenuScene.hscrList.setAdapter(hscore_adapter)	;	
			    
				MenuScene.progressBar.setVisibility(View.GONE);
				MenuScene.refreshButton.setVisibility(View.VISIBLE);

				
			} catch (JSONException e) {
				
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			
//			
							
			}
			finally
			{
			//	AfterGame.b1.setVisibility(View.VISIBLE);
				//AfterGame.bar.setVisibility(View.GONE);
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

	

}
	ArrayList< Leader> JsonToList(JSONArray lis)
	{
		ArrayList<Leader> leaderList=new ArrayList<Leader>();
		for(int i=0;i<lis.length();i++)
		{

			Leader a=new Leader();
			try
			{
			a.name=lis.getJSONObject(i).getString("Name");
			a.score=lis.getJSONObject(i).getString("hightoday");
			a.college=lis.getJSONObject(i).getString("college");
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
			       
					
			}
		}
		return leaderList;
	}
	}


