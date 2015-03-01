package com.cetdhwani.bottlejump;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cetdhwani.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;




public class FacebookLogin extends Activity {
	com.facebook.widget.LoginButton loginb;
	static Context a;
	String tok="";
	String ph="",nam="",email="",age="",link="",gen="",name="";
	private UiLifecycleHelper uiHelper;
	static int complete_send=0;
	static PopupWindow popupWindow;
	private boolean pendingPublishReauthorization = false;
	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
	public void call(Session session, SessionState state,
					Exception exception) {
		if (state.isOpened()) {
			try
			{
		
				onSessionStateChange(session, state, exception);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			// if the session is already open, try to show the selection fragment
			


		
		} else if (state.isClosed()) {
			
		exception.printStackTrace();
			loginb.setVisibility(View.VISIBLE);
			Toast.makeText(getApplicationContext(), ""+exception, Toast.LENGTH_SHORT).show();
		}
	}
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);
		Localstore A=new Localstore(getApplicationContext()); 
		String restoredText = A.getdata("asdf");
		Log.i("token", restoredText);
		setContentView(R.layout.loader);

		if(!restoredText.equals(""))
		{

	    	
		 a=getApplicationContext();
		 Log.i("restoredtext",restoredText );
new SendData(restoredText, "http://cetdhwani.com/bottlerun/signin.php",0,getApplicationContext(),this).execute("");	 
		}
		else
		{
		setContentView(R.layout.activity_facebook_login);
		loginb=(com.facebook.widget.LoginButton)findViewById(R.id.login_button);
		 loginb.setReadPermissions(Arrays.asList("email"));
		 a=getApplicationContext();
		}
    }


    void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	    	loginb.setVisibility(View.INVISIBLE);
	    	if(!pendingPublishReauthorization )
	    	{
	    	 final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	 	    if (session != null){
	 	        List<String> permissions = session.getPermissions();
	 	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	 	            pendingPublishReauthorization = true;
	 	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	 	                    .NewPermissionsRequest(this, PERMISSIONS);
	 	        session.requestNewPublishPermissions(newPermissionsRequest);
	 	            return;
	 	        }
	 	       Log.i("sessionstate","error");
	 	       pendingPublishReauthorization = false;
	            tok=session.getAccessToken();
	            Log.i("Accestoken", tok);
		    	//loginb.setVisibility(View.INVISIBLE);
			    sendAccessToken();
			   
	 	        }}
	 	    else if (pendingPublishReauthorization) 
	 	    {
	 	    	
		            pendingPublishReauthorization = false;
		            tok=session.getAccessToken();
		            Log.i("Accestoken", tok);
			    	loginb.setVisibility(View.INVISIBLE);
				    sendAccessToken();
		        
	 	    }
	 	    
		    
	        }
	     else if (state.isClosed()) {
	    	
	    }
	}
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e("Activity", String.format("Error: %s", error.toString()));
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i("Activity", "Success!");
	        }
	    });
	}
    void sendAccessToken()
    {
    	Localstore A=new Localstore(getApplicationContext());
    	 A.putdata("asdf", tok);
    	 if(this!=null)
    	
    	 
    		new SendData(tok, "http://cetdhwani.com/bottlerun/signin.php",0,getApplicationContext(),this).execute("");

    	
    }
    @Override
	public void onResume() {
	
	super.onResume();
	uiHelper.onResume();
	//if
}
@Override
	public void onPause() {
	super.onPause();
	
	uiHelper.onPause();
}
@Override
public void onSaveInstanceState(Bundle savedState) {
super.onSaveInstanceState(savedState);
uiHelper.onSaveInstanceState(savedState);
}
private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	  for (String string : subset) {
	     if (!superset.contains(string)) {
	 
	         return false; 
	     } 
	  } 
	  return true; 
	}
}
