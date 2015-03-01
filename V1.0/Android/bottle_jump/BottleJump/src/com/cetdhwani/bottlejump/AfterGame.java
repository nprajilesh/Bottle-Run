
package com.cetdhwani.bottlejump;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.cetdhwani.R;

public class AfterGame extends Activity {
	private ViewFlipper viewFlipper;
    private float lastX;
    static ListView list1;
    static ListView list2;
	 static ArrayList<leader> leaderList=null;
	 static ArrayList<String> couponList=null;
static Context aftergame;
 static View bar;
 static Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	aftergame =getApplicationContext();
        setContentView(R.layout.activity_after_game);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        b1=(Button)findViewById(R.id.ref);
        bar=findViewById(R.id.prog1);
//     
      try {
		
		JSONObject data=new JSONObject();
        try {
			data.put("token", new Localstore(getApplicationContext()).getdata("asdf"));
			data.put("highest", 0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        list1=(ListView)findViewById(R.id.list);
       new SendData(data.toString(), "http://cetdhwani.com/bottlerun/leaderboard.php", 3, AfterGame.aftergame).execute();
       MyAdapter adapter=new MyAdapter(AfterGame.aftergame,leaderList);
		list1.setAdapter(adapter);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
  //Getting My Wallet
      JSONObject data=new JSONObject();
      try {
			data.put("token", new Localstore(getApplicationContext()).getdata("asdf"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      list2=(ListView)findViewById(R.id.list2);
     //new SendData(data.toString(), "http://cetdhwani.com/bottlerun/wallet.php", 4, AfterGame.aftergame).execute();
     WAlletMyAdapter adapter=new WAlletMyAdapter(AfterGame.aftergame,couponList);
		list2.setAdapter(adapter);
      
    }

    // Using the following method, we will handle all screen swaps.
    public boolean onTouchEvent(MotionEvent touchevent) {
   /* 	switch (touchevent.getAction()) {
        
        case MotionEvent.ACTION_DOWN: 
        	lastX = touchevent.getX();
            break;
        case MotionEvent.ACTION_UP: 
            float currentX = touchevent.getX();
            
            // Handling left to right screen swap.
            if (lastX < currentX) {
            	
            	// If there aren't any other children, just break.
                if (viewFlipper.getDisplayedChild() == 0)
                	break;
                
                // Next screen comes in from left.
                viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
                // Current screen goes out from right. 
                viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);
                
                // Display next screen.
           //     viewFlipper.showNext();
             }
                                     
            // Handling right to left screen swap.
             if (lastX > currentX) {
            	 
            	 // If there is a child (to the left), kust break.
            	 if (viewFlipper.getDisplayedChild() == 1)
            		 break;
    
            	 // Next screen comes in from right.
            	 viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
            	// Current screen goes out from left. 
            	 viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
                 
            	// Display previous screen.
              //   viewFlipper.showPrevious();
             }
             break;
    	 }*/
         return false;
    }
    public void refresh(View v)
    {
    	JSONObject data=new JSONObject();
        try {
			data.put("token", new Localstore(aftergame).getdata("asdf"));
			data.put("highest", 0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      b1.setVisibility(View.INVISIBLE);
     bar.setVisibility(View.VISIBLE);
        new SendData(data.toString(), "http://cetdhwani.com/bottlerun/leaderboard.php", 3, AfterGame.aftergame).execute();

    }
    public void refresh2(View v)
    {
    	JSONObject data=new JSONObject();
        try {
			data.put("token", new Localstore(aftergame).getdata("asdf"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       //new SendData(data.toString(), "http://cetdhwani.com/bottlerun/wallet.php", 4, AfterGame.aftergame).execute();

    }
}