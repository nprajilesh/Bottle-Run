package com.cetdhwani.bottlejump;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

import cet.magic75.dhwani.GameActivity;

import com.cetdhwani.R;
import com.cetdhwani.scenes.MenuScene;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FBlogin extends Activity {
	Localstore store;
	String tok = "";
	boolean pendingPublishReauthorization = false;
	final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	static com.facebook.widget.LoginButton loginb;
	private UiLifecycleHelper uiHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MCrypt.SecretKey = "eru56bbhr7fdfu53";
		MCrypt.iv = "jnjnfvuABklasjjd";
		store = new Localstore(this);
		
		String post = "";
		post = store.getdata("p");
		tok = store.getdata("asdf");
		
		setContentView(R.layout.activity_facebook_login);
		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);
		loginb = (com.facebook.widget.LoginButton) findViewById(R.id.login_button);
		loginb.setReadPermissions(Arrays.asList("email"));
		LoadViews();
		if ((!tok.equals("")) && post.trim().equals("got")) {
			Intent i = new Intent(this, GameActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			finish();

		} else if(!tok.equals("")&&!isConnectingToInternet())
		{
			Intent i = new Intent(this, GameActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			finish();
		}
		else {
			loginb.setVisibility(View.VISIBLE);
			Session session = Session.getActiveSession();
			if (session != null) {
				if (!pendingPublishReauthorization) {
					List<String> permissions = session.getPermissions();

					if (!isSubsetOf(PERMISSIONS, permissions)) {
					pendingPublishReauthorization = true;
						Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
								this, PERMISSIONS);
						try
						{
						session.requestNewPublishPermissions(newPermissionsRequest);
						}
						catch(Exception e)
						{
							
						}

						return;
					}

				}
			}
		}
		sendAccessToken();
	}

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
			if (state.isOpened()) {
				try {
					tok=session.getAccessToken();
					List<String> permissions = session.getPermissions();
					if (isSubsetOf(PERMISSIONS, permissions)) {
					store.putdata("p", "got");
					}
					
					if(!pendingPublishReauthorization&&!store.getdata("college").equals(""))
					{
						Intent i=new Intent(FBlogin.this,GameActivity.class);
						sendAccessToken();
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						finish();
						startActivity(i);
					}
					loginb.setVisibility(View.INVISIBLE);
					aView.setVisibility(View.VISIBLE);
					submit.setVisibility(View.VISIBLE);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				// if the session is already open, try to show the selection
				// fragment
				
			} else if (state.isClosed()) {
				if(!pendingPublishReauthorization&&!store.getdata("college").equals(""))
				{
					Intent i=new Intent(FBlogin.this,GameActivity.class);
					sendAccessToken();
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					finish();
					startActivity(i);
				}
				Toast.makeText(getApplicationContext(), "Error Occured",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	private AutoCompleteTextView aView;
	private Button submit;
	private View progress;

	void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		
		
		List<String> permissions = session.getPermissions();
		if (isSubsetOf(PERMISSIONS, permissions)) {
			store.putdata("p", "got");
		}

		if (pendingPublishReauthorization && state.toString().equals("OPENED_TOKEN_UPDATED")) {
		
			pendingPublishReauthorization = false;}

	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {

				return false;
			}
		}
		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		View view = findViewById(R.id.player_view);
		if (view != null) {
			view.setBackgroundResource(R.anim.playeranimate);
			AnimationDrawable frameAnimation = null;
			frameAnimation = (AnimationDrawable) view.getBackground();
			if (frameAnimation != null)
				if (hasFocus) {
					frameAnimation.start();
				} else {
					frameAnimation.stop();
				}
		}
	}

	void LoadViews() {
		aView = (AutoCompleteTextView) findViewById(R.id.colleges);
		submit = (Button) findViewById(R.id.submit);
		progress = findViewById(R.id.submit_progress);
		aView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> view, View child, int arg2,
					long arg3) {
				String collegename = view.getItemAtPosition(arg2).toString();
				Toast.makeText(FBlogin.this,
						"You selected " + view.getItemAtPosition(arg2), 3)
						.show();

			}
		});
		aView.setAdapter(store.getColleges());
	}

	public void submitCollege(View view) {
		String college = aView.getText().toString().trim();
		if (!college.equals("")) {
			store.putdata("college", college);
			sendAccessToken();
			progress.setVisibility(View.VISIBLE);
			Intent i=new Intent(FBlogin.this,GameActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			finish();
			startActivity(i);
		}else
		{
			Toast.makeText(getApplicationContext(), "Invalid data ", Toast.LENGTH_LONG).show();
		}
	}

	void sendAccessToken() {
		store.putdata("asdf", tok);
		if (this != null) {
			JSONObject sigin_data = new JSONObject();
			try {
				sigin_data.put("token", tok);
				sigin_data.put("college", store.getdata("college"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			new SendData(sigin_data.toString(),
					"http://cetdhwani.com/bottlerun/signin2.php", 0,
					getApplicationContext(), this).execute("");
		}
		

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback() {
					@Override
					public void onError(FacebookDialog.PendingCall pendingCall,
							Exception error, Bundle data) {
						
					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data) {
					}
				});
	}
	@Override
	public void onResume()
	{
		uiHelper.onResume();
		super.onResume();
	}
	@Override
	public void onPause()
	{
		uiHelper.onPause();
		super.onPause();
	}
	public  boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null) 
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null) 
                  for (int i = 0; i < info.length; i++) 
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
}
