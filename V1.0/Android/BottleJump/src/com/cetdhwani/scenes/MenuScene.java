package com.cetdhwani.scenes;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cetdhwani.R;
import com.cetdhwani.bottlejump.Localstore;
import com.cetdhwani.bottlejump.SendData;
import com.cetdhwani.managers.CamManager;
import com.cetdhwani.managers.ImgManager;
import com.cetdhwani.managers.SceneManager;
import com.cetdhwani.managers.SceneManager.Scenes;


public class MenuScene extends Scene{
	private SceneManager sM;
	private Sprite blurSprite;
	private AnimatedSprite themeSprite; 
	private TiledSprite gameOverSprite;
	private ButtonSprite playButton;
	private ButtonSprite hsButton;
	private ButtonSprite audButton;
	private ButtonSprite relButton;
	private ButtonSprite fbButton;

	private Sprite adSprite;
	private Localstore store;
	private AutoParallaxBackground autoParallaxBackground;
	private BitmapTextureAtlas adTexture;
	private TextureRegion adRegion;	
	private ImgManager iM;
	private CamManager cM;
	private VertexBufferObjectManager vbo;
	public static Activity activity;
	private final int MARGIN=10;
	private final int GAME_OVER=0,GAME_PAUSE=1;
	private ButtonSprite abtButton;
	private AnimatedSprite playerSprite;
	private Dialog leaderDialogBox;
    private Dialog adDialog;
    private View adRoot;
    private Random random;
	private LinearLayout root;
	public static  Button refreshButton,closeButton,ldrButton,winnerButton,hscrButton;
	public static ListView ldrList;
	public static View progressBar;
	private String AD_URL="http://cetdhwani.com/bottlerun/ad.php";
	public static ListView hscrList;
	public static ListView winnerList;

	public MenuScene(SceneManager sM){
		this.sM=sM;
		iM=sM.rM.iM;
		cM=sM.rM.cM;
		random=new Random();
		activity=sM.rM.activity;
		vbo=sM.rM.engine.getVertexBufferObjectManager();
		autoParallaxBackground = new AutoParallaxBackground(
				0, 0, 0, 30);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f,
				new Sprite(0,2, iM.mParallaxLayerBack,vbo)));
		store =new Localstore(sM.rM.activity);

		setBackground(autoParallaxBackground);
		attachBlurSprite();
		attachGameOverSprite() ;
		attachplayGameSprite() ;		
		attachLeaderBoardSprite() ;
		attachAudioSprite(); 
		attachReloadSprite();
		attachAboutSprite();
		attachFBSprite();
		attachAdSprite();
		attachThemeSprite();
		attachPlayerSprite();
		loadLeaderBoxDialog();
		checkVersion();
	}
	private void checkVersion(){
		int version=store.getv();
		if(version==0||version==1)
			adSprite.setVisible(true);
	}
	public static boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
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
	private void attachAdSprite(){
		float left = this.cM.width / 2
				- iM.gameOverRegion.getWidth() / 2;
		float top = this.cM.height / 2+iM.playGameRegion.getHeight()/2;

		this.adSprite =new Sprite(left, top,
				iM.notifyRegion,
				vbo);
		attachChild(adSprite);
		adSprite.setVisible(false);
		
	}
	private void attachThemeSprite(){
		themeSprite = new AnimatedSprite(cM.width/2-iM.themeRegion.getWidth()/2, cM.height-iM.themeRegion.getHeight()-MARGIN, iM.themeRegion,
				vbo);
		themeSprite.animate(3000,true);
		attachChild(themeSprite);
	}
	private void attachBlurSprite() {
		blurSprite = new Sprite(0, 0, iM.blurRegion,
				vbo);
		attachChild(blurSprite);
	}
	public void setGameState(){
		if(store.getSoundVal())
			audButton.setCurrentTileIndex(0);
		else
			audButton.setCurrentTileIndex(1);

		MoveXModifier mod1=new MoveXModifier(0.4f,cM.width,gameOverSprite.getX());
		MoveXModifier mod2=new MoveXModifier(0.4f,-playButton.getX() ,playButton.getX()){
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				if(isConnectingToInternet()&&random.nextInt(3)==0)
					loadAdIntoWebView();
				
			}
	
		};
		MoveXModifier mod3=new MoveXModifier(0.4f,cM.width,relButton.getX()){

			
		};

		if(Values.GAME_OVER)
			gameOverSprite.setCurrentTileIndex(GAME_OVER);
		else if(Values.GAME_PAUSE){
			gameOverSprite.setCurrentTileIndex(GAME_PAUSE);
			relButton.setVisible(true);
			relButton.registerEntityModifier(mod3);

		}
		gameOverSprite.registerEntityModifier(mod1);		
		gameOverSprite.setVisible(true);		
		playButton.registerEntityModifier(mod2);

		
		
	}
	private void attachGameOverSprite() {
		float left = this.cM.width / 2
				- iM.gameOverRegion.getWidth() / 2;
		float top = this.cM.height / 2+iM.playGameRegion.getHeight()/2+10;
		gameOverSprite = new TiledSprite(left, top,
				iM.gameOverRegion,
				vbo);
		attachChild(gameOverSprite);
		gameOverSprite.setVisible(false);

	}
	private void attachplayGameSprite() {
		float left = this.cM.width / 2
				- iM.playGameRegion.getWidth() / 2;
		float top = this.cM.height / 2
				- iM.playGameRegion.getHeight()/2;
		playButton = new ButtonSprite(left, top,
				iM.playGameRegion,
				vbo) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				switch (pTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					setCurrentTileIndex(1);
					break;
				case TouchEvent.ACTION_UP:
					if(sM.gameScene==null)
						Values.GAME_PAUSE=false;
					if(!Values.GAME_PAUSE){
						sM.createGameScene();
						sM.setCurrentScene(Scenes.GAME);
						Values.loadInitVals();
					}
					else{
						if(relButton!=null)
						relButton.setVisible(false);
						sM.setCurrentScene(Scenes.GAME);						
						((GameScene) sM.gameScene).setResume();								
					}						
					gameOverSprite.setVisible(false);
					setCurrentTileIndex(0);
					break;
				default:
					setCurrentTileIndex(0);

				}
				return true;//super.onAreaTouched(pTouchEvent, pTouchAreaLocalX,
					//	pTouchAreaLocalY);
			}

		};
		attachChild(playButton);
		registerTouchArea(playButton);
		MoveXModifier mod2=new MoveXModifier(0.2f,-playButton.getX() ,playButton.getX());
		playButton.registerEntityModifier(mod2);

	}
	private void attachLeaderBoardSprite() {
		float top = MARGIN;
		float left =2*MARGIN+iM.musicRegion.getWidth(); 
				hsButton = new ButtonSprite(left, top,
				iM.leaderBoardRegion,
				vbo) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				switch (pTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					setCurrentTileIndex(1);
					break;
				case TouchEvent.ACTION_UP:
					activity.runOnUiThread(new Runnable() {
					     @Override
					     public void run() {
					    	 if(leaderDialogBox==null){
					    	 leaderDialogBox=new Dialog(activity,R.layout.leader_board);
					 		
					     	 leaderDialogBox.addContentView(root,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
					    	 }
							 getLeaderBoardView(root);
					    	 leaderDialogBox.show();
								
					     }});
					
					break;
				default:
					setCurrentTileIndex(0);

				}
				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}

		};
		attachChild(hsButton);
		registerTouchArea(hsButton);
		MoveXModifier mod2=new MoveXModifier(0.2f,cM.width ,hsButton.getX());
		hsButton.registerEntityModifier(mod2);

	}
	
	private void attachReloadSprite() {
		float top = cM.height/2-iM.relRegion.getHeight()/2;
		float left =cM.width/2+iM.playGameRegion.getWidth(0)/2+MARGIN; 
				relButton = new ButtonSprite(left, top,
				iM.relRegion,
				vbo) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				switch (pTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					setCurrentTileIndex(1);
					break;
				case TouchEvent.ACTION_UP:
						relButton.setVisible(false);
						((GameScene) sM.gameScene).restartFromPause();
						sM.createGameScene();
						sM.setCurrentScene(Scenes.GAME);						
					break;
				default:
					setCurrentTileIndex(0);

				}
				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}

		};
		attachChild(relButton);
		registerTouchArea(relButton);
		MoveXModifier mod2=new MoveXModifier(0.2f,cM.width ,relButton.getX());
		relButton.registerEntityModifier(mod2);
		relButton.setVisible(false);
	}
	private void attachAboutSprite() {
		float top = MARGIN;
		float left =cM.width-iM.relRegion.getWidth()-MARGIN;//-iM.abtRegion.getWidth()-MARGIN; 
				abtButton = new ButtonSprite(left, top,
				iM.abtRegion,
				vbo) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				switch (pTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					setCurrentTileIndex(1);
					break;
				case TouchEvent.ACTION_UP:
					activity.runOnUiThread(new Runnable() {
					     @Override
					     public void run() {
					    	 LayoutInflater inflater = activity.getLayoutInflater();
					    	 RelativeLayout root = (RelativeLayout) inflater.inflate(R.layout.about, null);
					    	 
					    	 Dialog dialog=new Dialog(activity,R.layout.about);
					    	 dialog.show();
					    	 dialog.addContentView(root,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
					    	 Animation anim;
					    	 // load animations
					    	 anim = AnimationUtils.loadAnimation(activity,
					    	                 R.anim.move_up);
					    	 LinearLayout scroller=(LinearLayout) root.findViewById(R.id.scroller);
					    	 scroller.setAnimation(anim);
						     }});			
										
					
					break;
				default:
					setCurrentTileIndex(0);

				}
				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}

		};
		attachChild(abtButton);
		registerTouchArea(abtButton);
		MoveXModifier mod2=new MoveXModifier(0.2f,cM.width ,abtButton.getX());
		abtButton.registerEntityModifier(mod2);

	}

	
	private void attachAudioSprite() {
		float left = MARGIN;//this.cM.width / 2
		//		- iM.musicRegion.getWidth() / 2;
		float top = MARGIN;//this.cM.height/2+iM.musicRegion.getHeight()+20;
		audButton = new ButtonSprite(left, top,
				iM.musicRegion,
				vbo) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				switch (pTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					store.toggleSound();

					break;
				case TouchEvent.ACTION_UP:
					if(store.getSoundVal())
						setCurrentTileIndex(0);
					else
						setCurrentTileIndex(1);

					break;
				default:
					if(store.getSoundVal())
						setCurrentTileIndex(0);
					else
						setCurrentTileIndex(1);

				}
				return true;
			}

		};
		if(store.getSoundVal())
			audButton.setCurrentTileIndex(0);
		else
			audButton.setCurrentTileIndex(1);
		attachChild(audButton);
		registerTouchArea(audButton);
		MoveXModifier mod2=new MoveXModifier(0.2f,cM.width ,audButton.getX());
		audButton.registerEntityModifier(mod2);

	}
public void attachFBSprite(){
	float top = MARGIN;
	float left =cM.width-2*(MARGIN+iM.fbRegion.getWidth());
	Log.d("width",""+iM.fbRegion.getWidth());
			fbButton = new ButtonSprite(left, top,
			iM.fbRegion,
			vbo) {
		@Override
		public boolean onAreaTouched(TouchEvent pTouchEvent,
				float pTouchAreaLocalX, float pTouchAreaLocalY) {
			switch (pTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				fbButton.setCurrentTileIndex(1);
							break;
			case TouchEvent.ACTION_UP:
				activity.runOnUiThread(new Runnable() {
				     @Override
				     public void run() {				    	 
//				    Intent fbActivity=new Intent(activity,MainActivity.class);				    
//				    activity.startActivity(fbActivity);
					 }});			
									
				fbButton.setCurrentTileIndex(0);
							
				break;
			default:
				fbButton.setCurrentTileIndex(0);
				
			}
			return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX,
					pTouchAreaLocalY);
		}

	};
	
	attachChild(fbButton);
	registerTouchArea(fbButton);
	MoveXModifier mod2=new MoveXModifier(0.2f,cM.width ,fbButton.getX());
	fbButton.registerEntityModifier(mod2);
	
}
	
	public View loadAdIntoWebView(){

		activity.runOnUiThread(new Runnable() {

			@Override
		     public void run() {
				if(adDialog==null){
			    	 adDialog=new Dialog(activity,R.layout.adview);	 		

					LayoutInflater inflater = activity.getLayoutInflater();
					
			    	adRoot = inflater.inflate(R.layout.adview, null);
			 		
			    	 closeButton=(Button) adRoot.findViewById(R.id.close_ad);
			     	 adDialog.addContentView(adRoot,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			     	closeButton.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							adDialog.hide();
						}
			     		
			     	})	;	
				}
		    	 WebView wv = (WebView) adRoot .findViewById(R.id.webview);
			        wv.getSettings().setJavaScriptEnabled(true);
			        activity.setProgressBarVisibility(true);
			        wv.setWebChromeClient(new WebChromeClient() {

			            public void onProgressChanged(WebView view, int progress) {
			                activity.setTitle("Loading...");
			                activity.setProgress(progress * 100);
			                if (progress == 100)
			                    activity.setTitle("Bottle jump");
			            }
			        });
			        wv.loadUrl(AD_URL);
			        wv.setWebViewClient(new WebViewClient());		    	 
		    	 adDialog.show();
				
		     }});
			
		
			        return null;
	}
	public LinearLayout getLeaderBoardView(LinearLayout root){
	    try {
			
			JSONObject data=new JSONObject();
	        try {
				data.put("token", store.getdata("asdf"));
				data.put("highest", 0);
			} catch (JSONException e) {
			}
	        String URL="http://cetdhwani.com/bottlerun/leaderboard2.php";
	      new SendData(data.toString(), URL, 3, activity).execute();
	      
	    } catch (Exception e) {
			e.printStackTrace();
			
		}
		 return root;
	}
	private void attachPlayerSprite(){
		float left=cM.width/2-iM.loaderRegion.getWidth();
		float top=cM.height/2-iM.playerRegion.getHeight()/2;
		playerSprite=new AnimatedSprite(left, top, iM.playerRegion,vbo );
		attachChild(playerSprite);
		playerSprite.animate(new long[]{100,100},true);
	}
	private void loadLeaderBoxDialog(){
   	 LayoutInflater inflater = activity.getLayoutInflater();
		   root = (LinearLayout) inflater.inflate(R.layout.leader_board, null);
		   
				    ldrList=(ListView)root.findViewById(R.id.leaders);
				    hscrList=(ListView)root.findViewById(R.id.hscore);
				    winnerList=(ListView)root.findViewById(R.id.winners);
				    
				    progressBar=root.findViewById(R.id.progress);
			 		refreshButton=(Button)root.findViewById(R.id.refresh);
			 		
			 		ldrButton=(Button)root.findViewById(R.id.ldrbutton);
			 		winnerButton=(Button)root.findViewById(R.id.winnerbutton);
			 		hscrButton=(Button)root.findViewById(R.id.hscrbutton);
			 		winnerButton.setBackgroundResource(R.drawable.disabletag);
			 		hscrButton.setBackgroundResource(R.drawable.disabletag);
			 		ldrButton.setBackgroundResource(R.drawable.enabletag);

			 		/*refresh button*/
					refreshButton.setOnClickListener(new View.OnClickListener() {						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							refreshButton.setVisibility(View.GONE);
							 getLeaderBoardView(root);
								new SendData(store.getdata("ghjk"), "http://cetdhwani.com/bottlerun/update.php",2,activity).execute();
						progressBar.setVisibility(View.VISIBLE);


						}
					});
					/*leader button*/
					ldrButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
					//Toast.makeText(activity, "leader", 3).show();		
					winnerButton.setBackgroundResource(R.drawable.disabletag);
					ldrButton.setBackgroundResource(R.drawable.enabletag);
					hscrButton.setBackgroundResource(R.drawable.disabletag);
					winnerList.setVisibility(View.GONE);
					hscrList.setVisibility(View.GONE);
					ldrList.setVisibility(View.VISIBLE);

						}
					});
				/*winnerbutton*/
					winnerButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
						//	Toast.makeText(activity, "winnner", 3).show();		
							hscrButton.setBackgroundResource(R.drawable.disabletag);
							winnerButton.setBackgroundResource(R.drawable.enabletag);
							ldrButton.setBackgroundResource(R.drawable.disabletag);
							
							hscrList.setVisibility(View.GONE);
							ldrList.setVisibility(View.GONE);
							winnerList.setVisibility(View.VISIBLE);
					
						}
					});
		    	 /*hscrbutton*/
					hscrButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
						//	Toast.makeText(activity, "hscr", 3).show();		
							winnerButton.setBackgroundResource(R.drawable.disabletag);
							ldrButton.setBackgroundResource(R.drawable.disabletag);
							hscrButton.setBackgroundResource(R.drawable.enabletag);

							winnerList.setVisibility(View.GONE);
							ldrList.setVisibility(View.GONE);
							hscrList.setVisibility(View.VISIBLE);
							
						}
					});

	}
}
