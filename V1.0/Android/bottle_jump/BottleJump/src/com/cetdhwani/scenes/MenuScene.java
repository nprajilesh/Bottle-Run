package com.cetdhwani.scenes;
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

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cetdhwani.bottlejump.AfterGame;
import com.cetdhwani.bottlejump.Localstore;
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
	private Sprite adSprite;
	private Localstore store;
	private AutoParallaxBackground autoParallaxBackground;
	private BitmapTextureAtlas adTexture;
	private TextureRegion adRegion;	
	private ImgManager iM;
	private CamManager cM;
	private VertexBufferObjectManager vbo;
	private Activity activity;
	private final int MARGIN=10;
	private final int GAME_OVER=0,GAME_PAUSE=1;
	private ButtonSprite abtButton;
	public MenuScene(SceneManager sM){
		this.sM=sM;
		iM=sM.rM.iM;
		cM=sM.rM.cM;
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
		attachAdSprite();
		attachThemeSprite();
	//	checkVersion();
	}
//	private void checkVersion(){
//		int version=store.getv();
	//	if(version==0||version==1)
		//	adSprite.setVisible(true);
//		Log.d("ver",""+version);
//	}
	private void attachAdSprite(){
		float left = this.cM.width / 2
				- iM.gameOverRegion.getWidth() / 2;
		float top = this.cM.height / 2+iM.playGameRegion.getHeight()+iM.leaderBoardRegion.getHeight()+10;

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
		MoveXModifier mod2=new MoveXModifier(0.4f,-playButton.getX() ,playButton.getX());
		MoveXModifier mod3=new MoveXModifier(0.4f,cM.width,relButton.getX());

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
					if(!Values.GAME_PAUSE){
						sM.createGameScene();
						sM.setCurrentScene(Scenes.GAME);
					}
					else{
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
				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
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
					Intent intent=new Intent(activity,AfterGame.class);
					activity.startActivity(intent);					
					setCurrentTileIndex(0);
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

	private void loadAd(String name){
		detachChild(adSprite);

		adTexture = new BitmapTextureAtlas(iM.tM, 256,
				64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		adRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				adTexture, sM.rM.activity, name, 0, 0);
		adTexture.load();
		float left = cM.width / 2
				- iM.gameOverRegion.getWidth() / 2;
		float top = cM.height / 2+iM.playGameRegion.getHeight()+iM.leaderBoardRegion.getHeight()+10;

		this.adSprite =new Sprite(left, top,
				iM.notifyRegion,
				vbo);
		attachChild(adSprite);

	//	this.adSprite.set
	}


}
